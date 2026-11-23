package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig

class AwardShardsUseCase {

    operator fun invoke(spinWin: Int, bet: Int): Int {
        if (spinWin <= 0) return 0
        val scaled = 1 + spinWin / (bet * 6)
        return scaled.coerceAtMost(GameConfig.MAX_SHARDS_PER_WIN)
    }
}
