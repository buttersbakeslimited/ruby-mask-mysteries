package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.ReelSymbol
import kotlin.random.Random

class SpinReelsUseCase {

    fun seedFor(sessionSeed: Long, spinIndex: Int): Long =
        (spinIndex + 1).toLong() * 7919L xor (sessionSeed * 104729L)

    operator fun invoke(seed: Long, pityActive: Boolean): List<ReelSymbol> {
        val random = Random(seed)
        val pool = weightedPool(pityActive)
        return List(GameConfig.CELLS) { pool[random.nextInt(pool.size)] }
    }

    private fun weightedPool(pityActive: Boolean): List<ReelSymbol> {
        val pool = ArrayList<ReelSymbol>()
        for (symbol in ReelSymbol.entries) {
            val boosted = if (pityActive && symbol.isWild) {
                (symbol.weight * 14 + 5) / 10
            } else {
                symbol.weight
            }
            repeat(boosted) { pool.add(symbol) }
        }
        return pool
    }
}
