package com.Fidmowe7.Loskow.presentation.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.Mission
import com.Fidmowe7.Loskow.domain.model.MissionId
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MissionsUiState(
    val missions: List<Mission>,
    val mapNode: Int,
    val shards: Int
) {
    val allClaimed: Boolean
        get() = missions.isNotEmpty() && missions.all { it.claimed }
}

class MissionsViewModel(private val cases: CaseRepository) : ViewModel() {

    val uiState: StateFlow<MissionsUiState> = cases.state
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(cases.state.value)
        )

    fun claim(id: MissionId) {
        cases.update { saved ->
            val progress = saved.missionProgress[id] ?: 0
            if (progress < id.target || saved.missionClaimed.contains(id)) {
                saved
            } else {
                saved.copy(
                    shards = saved.shards + id.shardReward,
                    missionClaimed = saved.missionClaimed + id,
                    mapNode = (saved.mapNode + 1).coerceAtMost(GameConfig.MAP_NODES)
                )
            }
        }
    }

    private fun toUiState(state: CaseState): MissionsUiState = MissionsUiState(
        missions = MissionId.entries.map { id ->
            Mission(
                id = id,
                progress = state.missionProgress[id] ?: 0,
                claimed = state.missionClaimed.contains(id)
            )
        },
        mapNode = state.mapNode,
        shards = state.shards
    )
}
