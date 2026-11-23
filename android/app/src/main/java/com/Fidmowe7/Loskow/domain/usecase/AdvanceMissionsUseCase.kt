package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.domain.model.MissionId

data class MissionTrigger(
    val spins: Int = 0,
    val threeMasks: Boolean = false,
    val clueStreakFour: Boolean = false,
    val tilesUnlocked: Int = 0,
    val vaultVisits: Int = 0,
    val rewardClaimed: Boolean = false
)

class AdvanceMissionsUseCase {

    operator fun invoke(
        progress: Map<MissionId, Int>,
        trigger: MissionTrigger,
        streakMultiplier: Int
    ): Map<MissionId, Int> {
        val next = HashMap(progress)
        add(next, MissionId.SpinTwentyFive, trigger.spins * streakMultiplier)
        if (trigger.threeMasks) add(next, MissionId.ThreeMasks, 1)
        if (trigger.clueStreakFour) add(next, MissionId.ClueStreakFour, 1)
        add(next, MissionId.UnlockThreeTiles, trigger.tilesUnlocked)
        add(next, MissionId.VaultTwice, trigger.vaultVisits)
        if (trigger.rewardClaimed) add(next, MissionId.ClaimNightly, 1)
        return next
    }

    fun streakMultiplier(streak: Int): Int = 1 + (streak.coerceAtMost(4) / 4)

    private fun add(target: HashMap<MissionId, Int>, id: MissionId, amount: Int) {
        if (amount <= 0) return
        val current = target[id] ?: 0
        target[id] = (current + amount).coerceAtMost(id.target)
    }
}
