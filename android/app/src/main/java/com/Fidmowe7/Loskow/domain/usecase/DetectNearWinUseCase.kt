package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.ReelSymbol

class DetectNearWinUseCase {

    operator fun invoke(grid: List<ReelSymbol>): Boolean {
        for (row in 0 until GameConfig.PAYLINES) {
            val cells = (0 until GameConfig.REELS).map { grid[row * GameConfig.REELS + it] }
            val high = cells.filter { it.isHigh }
            if (high.size < 2) continue
            val leader = high.first()
            val matching = cells.count { it == leader }
            if (matching == 2) return true
        }
        return false
    }
}
