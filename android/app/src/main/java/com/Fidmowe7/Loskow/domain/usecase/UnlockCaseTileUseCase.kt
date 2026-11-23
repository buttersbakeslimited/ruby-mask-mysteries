package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.CaseState

data class UnlockOutcome(
    val state: CaseState,
    val unlocked: Boolean,
    val caseCompleted: Boolean
)

class UnlockCaseTileUseCase {

    operator fun invoke(state: CaseState, tileIndex: Int): UnlockOutcome {
        if (state.unlockedTiles.contains(tileIndex)) return UnlockOutcome(state, false, false)
        if (state.shards < GameConfig.TILE_SHARD_COST) return UnlockOutcome(state, false, false)
        val tiles = state.unlockedTiles + tileIndex
        val caseIndex = tileIndex / GameConfig.TILES_PER_CASE
        val first = caseIndex * GameConfig.TILES_PER_CASE
        val completed = (first until first + GameConfig.TILES_PER_CASE).all { tiles.contains(it) }
        val bonus = if (completed) {
            state.caseBonusPercent + GameConfig.CASE_BONUS_STEP
        } else {
            state.caseBonusPercent
        }
        return UnlockOutcome(
            state.copy(
                shards = state.shards - GameConfig.TILE_SHARD_COST,
                unlockedTiles = tiles,
                caseBonusPercent = bonus
            ),
            true,
            completed
        )
    }
}
