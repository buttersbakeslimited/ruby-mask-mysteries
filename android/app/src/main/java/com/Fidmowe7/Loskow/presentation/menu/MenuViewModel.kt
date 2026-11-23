package com.Fidmowe7.Loskow.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.ui.BoardRow
import com.Fidmowe7.Loskow.core.util.todayEpochDay
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.MissionId
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.ClaimDailyRewardUseCase
import com.Fidmowe7.Loskow.domain.usecase.RankLeaderboardUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(cases: CaseRepository) : ViewModel() {

    private val ranking = RankLeaderboardUseCase()
    private val rewards = ClaimDailyRewardUseCase()

    val uiState: StateFlow<MenuUiState> = cases.state
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(cases.state.value)
        )

    private fun toUiState(state: CaseState): MenuUiState {
        val board = if (state.bestSessionTotal <= 0) {
            emptyList()
        } else {
            val full = ranking(SampleData.rivalScores, state.bestSessionTotal)
            val shown = full.take(3).toMutableList()
            val player = full.firstOrNull { it.isPlayer }
            if (player != null && shown.none { it.isPlayer }) {
                shown.add(player)
            }
            shown.map { entry ->
                    BoardRow(
                        rank = entry.rank,
                        nameRes = if (entry.isPlayer) {
                            R.string.rival_you
                        } else {
                            SampleData.rivalNames[entry.rivalIndex]
                        },
                        score = entry.score,
                        isPlayer = entry.isPlayer
                    )
                }
        }
        val missionReady = MissionId.entries.any { id ->
            val progress = state.missionProgress[id] ?: 0
            progress >= id.target && !state.missionClaimed.contains(id)
        }
        val totalTiles = GameConfig.CASE_COUNT * GameConfig.TILES_PER_CASE
        return MenuUiState(
            credits = state.credits,
            shards = state.shards,
            board = board,
            rewardReady = rewards.claimable(state, todayEpochDay()),
            missionReady = missionReady,
            collectionReady = state.shards >= GameConfig.TILE_SHARD_COST &&
                state.unlockedTiles.size < totalTiles,
            caseBonusPercent = state.caseBonusPercent
        )
    }
}
