package com.Fidmowe7.Loskow.domain.model

data class PaylineWin(val line: Int, val symbol: ReelSymbol, val amount: Int)

data class SpinResult(
    val seed: Long,
    val grid: List<ReelSymbol>,
    val wins: List<PaylineWin>,
    val baseWin: Int,
    val totalWin: Int,
    val nearWin: Boolean,
    val maskCount: Int,
    val shards: Int
) {
    val isWin: Boolean
        get() = totalWin > 0
}
