package com.Fidmowe7.Loskow.domain.model

data class CaseState(
    val credits: Int,
    val shards: Int,
    val bestSessionTotal: Int,
    val bestWin: Int,
    val totalSpins: Int,
    val unlockedTiles: Set<Int>,
    val caseBonusPercent: Int,
    val missionProgress: Map<MissionId, Int>,
    val missionClaimed: Set<MissionId>,
    val mapNode: Int,
    val rewardDay: Int,
    val lastClaimEpochDay: Long,
    val vaultVisits: Int
)
