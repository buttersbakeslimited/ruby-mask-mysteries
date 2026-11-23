package com.Fidmowe7.Loskow.presentation.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.util.todayEpochDay
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.FeaturePreview
import com.Fidmowe7.Loskow.domain.model.PreviewStatus
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.ClaimDailyRewardUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LobbyUiState(val previews: List<FeaturePreview>)

class LobbyViewModel(cases: CaseRepository) : ViewModel() {

    private val rewards = ClaimDailyRewardUseCase()

    val uiState: StateFlow<LobbyUiState> = cases.state
        .map { toUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = toUiState(cases.state.value)
        )

    private fun toUiState(state: CaseState): LobbyUiState {
        val nightly = if (rewards.claimable(state, todayEpochDay())) {
            PreviewStatus.Ready
        } else {
            PreviewStatus.Claimed
        }
        val clue = if (state.mapNode >= 3) PreviewStatus.Unlocked else PreviewStatus.Locked
        return LobbyUiState(
            previews = listOf(
                FeaturePreview(0, 1, PreviewStatus.Unlocked),
                FeaturePreview(1, 2, PreviewStatus.Unlocked),
                FeaturePreview(2, 3, clue),
                FeaturePreview(3, 4, nightly)
            )
        )
    }
}
