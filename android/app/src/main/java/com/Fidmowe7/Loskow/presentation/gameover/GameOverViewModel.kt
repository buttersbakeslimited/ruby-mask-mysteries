package com.Fidmowe7.Loskow.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.ui.BoardRow
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.RankLeaderboardUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GameOverUiState(
    val board: List<BoardRow>,
    val playerRank: Int,
    val credits: Int,
    val shards: Int,
    val needsRefill: Boolean
)

class GameOverViewModel(private val cases: CaseRepository) : ViewModel() {

    private val ranking = RankLeaderboardUseCase()

    val uiState: StateFlow<GameOverUiState> = cases.state
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(cases.state.value)
        )

    fun grantRefill() {
        cases.update { saved ->
            if (saved.credits >= GameConfig.BET) {
                saved
            } else {
                saved.copy(credits = saved.credits + GameConfig.REFILL_CREDITS)
            }
        }
    }

    private fun toUiState(state: CaseState): GameOverUiState {
        val full = ranking(SampleData.rivalScores, state.bestSessionTotal)
        val shown = full.take(3).toMutableList()
        val player = full.firstOrNull { it.isPlayer }
        if (player != null && shown.none { it.isPlayer }) {
            shown.add(player)
        }
        return GameOverUiState(
            board = shown.map { entry ->
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
            },
            playerRank = ranking.playerRank(full),
            credits = state.credits,
            shards = state.shards,
            needsRefill = state.credits < GameConfig.BET
        )
    }
}
