package com.Fidmowe7.Loskow.domain.model

enum class VaultPrize(val credits: Int, val shards: Int, val clues: Int) {
    CreditsSmall(120, 0, 0),
    CreditsLarge(200, 0, 0),
    ShardsSmall(0, 2, 0),
    ShardsLarge(0, 3, 0),
    CaseClue(0, 0, 1),
    CollectAll(0, 0, 0)
}

data class VaultDoor(
    val index: Int,
    val prize: VaultPrize,
    val revealed: Boolean
)
