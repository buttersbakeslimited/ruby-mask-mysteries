package com.Fidmowe7.Loskow.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.navigation.Screen
import com.Fidmowe7.Loskow.domain.model.ReelSymbol
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository
import com.Fidmowe7.Loskow.domain.usecase.AdvanceMissionsUseCase
import com.Fidmowe7.Loskow.domain.usecase.AwardShardsUseCase
import com.Fidmowe7.Loskow.domain.usecase.DetectNearWinUseCase
import com.Fidmowe7.Loskow.domain.usecase.EvaluatePaylinesUseCase
import com.Fidmowe7.Loskow.domain.usecase.MissionTrigger
import com.Fidmowe7.Loskow.domain.usecase.SpinReelsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val cases: CaseRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val spinReels = SpinReelsUseCase()
    private val evaluate = EvaluatePaylinesUseCase()
    private val detectNearWin = DetectNearWinUseCase()
    private val awardShards = AwardShardsUseCase()
    private val missions = AdvanceMissionsUseCase()

    private val backing = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = backing.asStateFlow()

    private val eventChannel = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = eventChannel.receiveAsFlow()

    private var spinJob: Job? = null
    private var sessionSeed: Long = 0L
    private var spinIndex: Int = 0
    private var lossStreak: Int = 0
    private var hotSpins: Int = 0
    private var breakShownAt: Int = 0

    init {
        viewModelScope.launch {
            cases.state.collect { saved ->
                backing.update {
                    it.copy(
                        credits = saved.credits,
                        caseBonusPercent = saved.caseBonusPercent,
                        mapNode = saved.mapNode
                    )
                }
            }
        }
        viewModelScope.launch {
            settings.settings.collect { options ->
                backing.update { it.copy(highContrast = options.highContrast) }
            }
        }
        restartSession()
    }

    fun restartSession() {
        spinJob?.cancel()
        spinJob = null
        sessionSeed = System.currentTimeMillis() / 1000L
        spinIndex = 0
        lossStreak = 0
        hotSpins = 0
        breakShownAt = 0
        val saved = cases.state.value
        val options = settings.settings.value
        backing.value = GameUiState(
            credits = saved.credits,
            grid = spinReels(sessionSeed, false),
            caseBonusPercent = saved.caseBonusPercent,
            mapNode = saved.mapNode,
            highContrast = options.highContrast,
            sessionSeed = sessionSeed
        )
    }

    fun pause() {
        spinJob?.cancel()
        spinJob = null
        backing.update { it.copy(phase = SpinPhase.Idle, stoppedReels = GameConfig.REELS) }
    }

    fun dismissBreakBanner() {
        backing.update { it.copy(breakBannerVisible = false) }
    }

    fun resetSessionCounter() {
        breakShownAt = 0
        backing.update { it.copy(sessionSpins = 0, breakBannerVisible = false) }
    }

    fun applyVaultHaul(credits: Int, shards: Int, clues: Int) {
        cases.update { saved ->
            val advanced = if (clues > 0) {
                (saved.mapNode + clues).coerceAtMost(GameConfig.MAP_NODES)
            } else {
                saved.mapNode
            }
            saved.copy(
                credits = saved.credits + credits,
                shards = saved.shards + shards,
                mapNode = advanced
            )
        }
        backing.update {
            it.copy(
                sessionTotal = it.sessionTotal + credits,
                sessionShards = it.sessionShards + shards,
                lastWin = if (credits > 0) credits else it.lastWin
            )
        }
    }

    fun endSession() {
        spinJob?.cancel()
        spinJob = null
        finish(false)
    }

    fun spin() {
        val snapshot = backing.value
        if (snapshot.phase != SpinPhase.Idle) return
        if (snapshot.credits < snapshot.bet) {
            finish(true)
            return
        }
        spinJob = viewModelScope.launch {
            val options = settings.settings.value
            val speed = GameConfig.SPIN_MS[options.speedIndex.coerceIn(0, 2)]
            val stagger = GameConfig.STAGGER_MS[options.speedIndex.coerceIn(0, 2)]
            val seed = spinReels.seedFor(sessionSeed, spinIndex)
            val pity = lossStreak >= GameConfig.PITY_AFTER_LOSSES && hotSpins == 0
            val target = spinReels(seed, pity)
            val nearRaw = detectNearWin(target)
            val near = nearRaw && options.nearWinHighlight
            spinIndex += 1

            cases.update { it.copy(credits = it.credits - snapshot.bet) }
            backing.update {
                it.copy(
                    phase = SpinPhase.Spinning,
                    stoppedReels = 0,
                    winningLines = emptySet(),
                    nearWinVisible = false,
                    lastWin = 0,
                    lastSpinSeed = seed
                )
            }

            val roller = launch {
                val noise = Random(seed xor 0x5DEECE66DL)
                while (isActive) {
                    backing.update { current ->
                        current.copy(
                            grid = List(GameConfig.CELLS) { cell ->
                                if (cell % GameConfig.REELS < current.stoppedReels) {
                                    target[cell]
                                } else {
                                    ReelSymbol.entries[noise.nextInt(ReelSymbol.entries.size)]
                                }
                            }
                        )
                    }
                    delay(70L)
                }
            }

            delay(speed.toLong())
            for (reel in 0 until GameConfig.REELS) {
                if (reel > 0) {
                    delay(stagger.toLong())
                }
                if (reel == GameConfig.REELS - 1 && near) {
                    delay(500L)
                }
                backing.update { it.copy(stoppedReels = reel + 1) }
            }
            roller.cancel()
            backing.update { it.copy(grid = target, stoppedReels = GameConfig.REELS) }

            resolve(target, near, nearRaw)
        }
    }

    private suspend fun resolve(grid: List<ReelSymbol>, near: Boolean, nearRaw: Boolean) {
        val bet = backing.value.bet
        val bonusPercent = cases.state.value.caseBonusPercent
        val wins = evaluate(grid, bet)
        val base = wins.sumOf { it.amount }
        val total = base + base * bonusPercent / 100
        val shards = awardShards(total, bet)
        val maskCount = grid.count { it.isWild }
        val streak = if (maskCount > 0) backing.value.clueStreak + 1 else 0

        lossStreak = if (total > 0) 0 else lossStreak + 1
        hotSpins = when {
            total >= bet * 10 -> 2
            hotSpins > 0 -> hotSpins - 1
            else -> 0
        }

        val multiplier = missions.streakMultiplier(streak)
        val trigger = MissionTrigger(
            spins = 1,
            threeMasks = maskCount >= 3,
            clueStreakFour = streak >= 4
        )
        cases.update { saved ->
            saved.copy(
                credits = saved.credits + total,
                shards = saved.shards + shards,
                bestWin = maxOf(saved.bestWin, total),
                totalSpins = saved.totalSpins + 1,
                missionProgress = missions(saved.missionProgress, trigger, multiplier)
            )
        }

        val meterNext = if (nearRaw) {
            (backing.value.meter + 1).coerceAtMost(GameConfig.METER_MAX)
        } else {
            backing.value.meter
        }
        val spins = backing.value.sessionSpins + 1
        val threshold = GameConfig.BREAK_OPTIONS[
            settings.settings.value.breakIndex.coerceIn(0, 2)
        ]
        val showBreak = spins >= threshold && spins - breakShownAt >= threshold
        if (showBreak) {
            breakShownAt = spins
        }

        backing.update {
            it.copy(
                phase = SpinPhase.Evaluating,
                lastWin = total,
                winningLines = wins.map { win -> win.line }.toSet(),
                nearWinVisible = near,
                meter = meterNext,
                clueStreak = streak,
                sessionSpins = spins,
                sessionTotal = it.sessionTotal + total,
                sessionBestWin = maxOf(it.sessionBestWin, total),
                sessionShards = it.sessionShards + shards,
                breakBannerVisible = it.breakBannerVisible || showBreak
            )
        }

        delay(GameConfig.WIN_ROLLUP_MS)
        if (near) {
            delay(GameConfig.NEAR_WIN_CAPTION_MS)
        }
        backing.update { it.copy(phase = SpinPhase.Idle, nearWinVisible = false) }

        val vaultDue = maskCount >= 3 || meterNext >= GameConfig.METER_MAX
        if (vaultDue) {
            delay(GameConfig.BONUS_HANDOFF_MS)
            cases.update { saved ->
                saved.copy(
                    vaultVisits = saved.vaultVisits + 1,
                    missionProgress = missions(
                        saved.missionProgress,
                        MissionTrigger(vaultVisits = 1),
                        1
                    )
                )
            }
            backing.update { it.copy(meter = 0) }
            eventChannel.send(GameEvent.OpenBonus)
            return
        }

        if (cases.state.value.credits < bet) {
            delay(GameConfig.OUT_OF_CREDITS_MS)
            finish(true)
        }
    }

    private fun finish(outOfCredits: Boolean) {
        val snapshot = backing.value
        cases.update { saved ->
            saved.copy(bestSessionTotal = maxOf(saved.bestSessionTotal, snapshot.sessionTotal))
        }
        val summary = Screen.GameOver(
            sessionTotal = snapshot.sessionTotal,
            bestWin = snapshot.sessionBestWin,
            spins = snapshot.sessionSpins,
            shards = snapshot.sessionShards,
            outOfCredits = outOfCredits
        )
        viewModelScope.launch { eventChannel.send(GameEvent.SessionEnded(summary)) }
    }
}
