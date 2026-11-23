package com.Fidmowe7.Loskow.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.util.hoursUntilTomorrow
import com.Fidmowe7.Loskow.core.util.todayEpochDay
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.RewardDay
import com.Fidmowe7.Loskow.domain.model.RewardOutcome
import com.Fidmowe7.Loskow.domain.model.RewardState
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.AdvanceMissionsUseCase
import com.Fidmowe7.Loskow.domain.usecase.ClaimDailyRewardUseCase
import com.Fidmowe7.Loskow.domain.usecase.MissionTrigger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class RewardsUiState(
    val ladder: List<RewardDay> = emptyList(),
    val currentDay: Int = 1,
    val claimable: Boolean = false,
    val streakBroken: Boolean = false,
    val clockSkew: Boolean = false,
    val hoursLeft: Int = 0
)

class RewardsViewModel(private val cases: CaseRepository) : ViewModel() {

    private val claimReward = ClaimDailyRewardUseCase()
    private val missions = AdvanceMissionsUseCase()

    private val feedback = MutableStateFlow(RewardOutcome.AlreadyClaimed)

    val uiState: StateFlow<RewardsUiState> = combine(
        cases.state,
        feedback
    ) { saved, outcome ->
        toUiState(saved, outcome)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = toUiState(cases.state.value, RewardOutcome.AlreadyClaimed)
    )

    fun claim() {
        val today = todayEpochDay()
        val claim = claimReward(
            cases.state.value,
            today,
            SampleData.rewardCredits,
            SampleData.rewardShards
        )
        feedback.value = claim.outcome
        if (claim.outcome != RewardOutcome.Granted) return
        cases.update { saved ->
            saved.copy(
                credits = saved.credits + claim.credits,
                shards = saved.shards + claim.shards,
                rewardDay = claim.day,
                lastClaimEpochDay = today,
                missionProgress = missions(
                    saved.missionProgress,
                    MissionTrigger(rewardClaimed = true),
                    1
                )
            )
        }
    }

    private fun toUiState(state: CaseState, outcome: RewardOutcome): RewardsUiState {
        val today = todayEpochDay()
        val claimable = claimReward.claimable(state, today)
        val nextDay = claimReward.dayFor(state, today)
        val ladder = (1..GameConfig.REWARD_DAYS).map { day ->
            val status = when {
                day < nextDay -> RewardState.Claimed
                day == nextDay && claimable -> RewardState.Ready
                day == nextDay -> RewardState.Claimed
                else -> RewardState.Locked
            }
            RewardDay(
                day = day,
                credits = SampleData.rewardCredits[day - 1],
                shards = SampleData.rewardShards[day - 1],
                state = status
            )
        }
        return RewardsUiState(
            ladder = ladder,
            currentDay = nextDay,
            claimable = claimable,
            streakBroken = claimReward.streakBroken(state, today) && state.rewardDay > 1,
            clockSkew = outcome == RewardOutcome.ClockSkew,
            hoursLeft = hoursUntilTomorrow()
        )
    }
}
