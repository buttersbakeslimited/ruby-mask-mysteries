package com.Fidmowe7.Loskow.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.CaseFolder
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.CaseTile
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.AdvanceMissionsUseCase
import com.Fidmowe7.Loskow.domain.usecase.MissionTrigger
import com.Fidmowe7.Loskow.domain.usecase.UnlockCaseTileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CollectionUiState(
    val folders: List<CaseFolder> = emptyList(),
    val shards: Int = 0,
    val caseBonusPercent: Int = 0,
    val shortOfShards: Boolean = false
)

private val CASE_SPRITES = intArrayOf(3, 2, 5)

class CollectionViewModel(private val cases: CaseRepository) : ViewModel() {

    private val unlockTile = UnlockCaseTileUseCase()
    private val missions = AdvanceMissionsUseCase()

    private val shortOfShards = MutableStateFlow(false)

    val uiState: StateFlow<CollectionUiState> = combine(
        cases.state,
        shortOfShards.asStateFlow()
    ) { saved, short ->
        toUiState(saved, short)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = toUiState(cases.state.value, false)
    )

    fun unlock(tileIndex: Int) {
        val outcome = unlockTile(cases.state.value, tileIndex)
        if (!outcome.unlocked) {
            shortOfShards.value = true
            return
        }
        shortOfShards.value = false
        cases.update {
            outcome.state.copy(
                missionProgress = missions(
                    outcome.state.missionProgress,
                    MissionTrigger(tilesUnlocked = 1),
                    1
                )
            )
        }
    }

    private fun toUiState(state: CaseState, short: Boolean): CollectionUiState {
        val folders = (0 until GameConfig.CASE_COUNT).map { caseIndex ->
            CaseFolder(
                index = caseIndex,
                tiles = (0 until GameConfig.TILES_PER_CASE).map { slot ->
                    val index = caseIndex * GameConfig.TILES_PER_CASE + slot
                    CaseTile(
                        index = index,
                        caseIndex = caseIndex,
                        spriteIndex = CASE_SPRITES[caseIndex],
                        unlocked = state.unlockedTiles.contains(index),
                        cost = GameConfig.TILE_SHARD_COST
                    )
                }
            )
        }
        return CollectionUiState(
            folders = folders,
            shards = state.shards,
            caseBonusPercent = state.caseBonusPercent,
            shortOfShards = short
        )
    }
}
