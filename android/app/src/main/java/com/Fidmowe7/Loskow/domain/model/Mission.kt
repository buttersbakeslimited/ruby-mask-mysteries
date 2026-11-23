package com.Fidmowe7.Loskow.domain.model

enum class MissionId(val target: Int, val shardReward: Int) {
    SpinTwentyFive(25, 4),
    ThreeMasks(1, 6),
    ClueStreakFour(1, 5),
    UnlockThreeTiles(3, 5),
    VaultTwice(2, 6),
    ClaimNightly(1, 3)
}

data class Mission(
    val id: MissionId,
    val progress: Int,
    val claimed: Boolean
) {
    val target: Int
        get() = id.target

    val complete: Boolean
        get() = progress >= id.target

    val claimable: Boolean
        get() = complete && !claimed

    val ratio: Float
        get() = if (id.target <= 0) 0f else (progress.toFloat() / id.target).coerceIn(0f, 1f)
}
