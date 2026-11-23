package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.RewardClaim
import com.Fidmowe7.Loskow.domain.model.RewardOutcome

class ClaimDailyRewardUseCase {

    fun dayFor(state: CaseState, today: Long): Int {
        if (state.lastClaimEpochDay <= 0L) return 1
        val gap = today - state.lastClaimEpochDay
        return when {
            gap <= 0L -> state.rewardDay.coerceIn(1, GameConfig.REWARD_DAYS)
            gap == 1L -> (state.rewardDay + 1).let { if (it > GameConfig.REWARD_DAYS) 1 else it }
            else -> 1
        }
    }

    fun streakBroken(state: CaseState, today: Long): Boolean =
        state.lastClaimEpochDay > 0L && today - state.lastClaimEpochDay > 1L

    fun claimable(state: CaseState, today: Long): Boolean =
        state.lastClaimEpochDay <= 0L || today > state.lastClaimEpochDay

    operator fun invoke(
        state: CaseState,
        today: Long,
        ladderCredits: List<Int>,
        ladderShards: List<Int>
    ): RewardClaim {
        if (today < state.lastClaimEpochDay) {
            return RewardClaim(RewardOutcome.ClockSkew, 0, 0, state.rewardDay, false)
        }
        if (!claimable(state, today)) {
            return RewardClaim(RewardOutcome.AlreadyClaimed, 0, 0, state.rewardDay, false)
        }
        val day = dayFor(state, today)
        val index = (day - 1).coerceIn(0, ladderCredits.size - 1)
        return RewardClaim(
            RewardOutcome.Granted,
            ladderCredits[index],
            ladderShards[index],
            day,
            streakBroken(state, today)
        )
    }
}
