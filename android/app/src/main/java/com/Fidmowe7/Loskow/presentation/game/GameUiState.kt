package com.Fidmowe7.Loskow.presentation.game

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.navigation.Screen
import com.Fidmowe7.Loskow.domain.model.ReelSymbol

enum class SpinPhase { Idle, Spinning, Evaluating }

data class GameUiState(
    val credits: Int = 0,
    val bet: Int = GameConfig.BET,
    val lastWin: Int = 0,
    val grid: List<ReelSymbol> = List(GameConfig.CELLS) { ReelSymbol.BallroomBell },
    val stoppedReels: Int = GameConfig.REELS,
    val phase: SpinPhase = SpinPhase.Idle,
    val winningLines: Set<Int> = emptySet(),
    val nearWinVisible: Boolean = false,
    val meter: Int = 0,
    val sessionSpins: Int = 0,
    val sessionTotal: Int = 0,
    val sessionBestWin: Int = 0,
    val sessionShards: Int = 0,
    val clueStreak: Int = 0,
    val mapNode: Int = 1,
    val caseBonusPercent: Int = 0,
    val highContrast: Boolean = false,
    val breakBannerVisible: Boolean = false,
    val sessionSeed: Long = 0L,
    val lastSpinSeed: Long = 0L
) {
    val meterReady: Boolean
        get() = meter >= GameConfig.METER_MAX

    val canSpin: Boolean
        get() = phase == SpinPhase.Idle && credits >= bet
}

sealed interface GameEvent {
    data object OpenBonus : GameEvent
    data class SessionEnded(val summary: Screen.GameOver) : GameEvent
}
