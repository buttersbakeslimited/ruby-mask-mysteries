package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.PaylineWin
import com.Fidmowe7.Loskow.domain.model.ReelSymbol

class EvaluatePaylinesUseCase {

    operator fun invoke(grid: List<ReelSymbol>, bet: Int): List<PaylineWin> {
        val wins = ArrayList<PaylineWin>()
        for (row in 0 until GameConfig.PAYLINES) {
            val cells = (0 until GameConfig.REELS).map { grid[row * GameConfig.REELS + it] }
            val symbol = lineSymbol(cells) ?: continue
            wins.add(PaylineWin(row, symbol, bet * symbol.payout))
        }
        return wins
    }

    private fun lineSymbol(cells: List<ReelSymbol>): ReelSymbol? {
        val named = cells.firstOrNull { !it.isWild }
        if (named == null) return ReelSymbol.RubyMask
        return if (cells.all { it == named || it.isWild }) named else null
    }
}
