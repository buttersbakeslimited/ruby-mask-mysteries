package com.Fidmowe7.Loskow.domain.model

data class CaseTile(
    val index: Int,
    val caseIndex: Int,
    val spriteIndex: Int,
    val unlocked: Boolean,
    val cost: Int
)

data class CaseFolder(
    val index: Int,
    val tiles: List<CaseTile>
) {
    val complete: Boolean
        get() = tiles.isNotEmpty() && tiles.all { it.unlocked }
}
