package com.Fidmowe7.Loskow.presentation.responsible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ResponsibleUiState(
    val breakThreshold: Int,
    val totalSpins: Int,
    val caseBonusPercent: Int
)

class ResponsibleGamingViewModel(
    cases: CaseRepository,
    settings: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<ResponsibleUiState> = combine(
        cases.state,
        settings.settings
    ) { saved, options ->
        ResponsibleUiState(
            breakThreshold = GameConfig.BREAK_OPTIONS[options.breakIndex.coerceIn(0, 2)],
            totalSpins = saved.totalSpins,
            caseBonusPercent = saved.caseBonusPercent
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ResponsibleUiState(
            breakThreshold = GameConfig.BREAK_OPTIONS[
                settings.settings.value.breakIndex.coerceIn(0, 2)
            ],
            totalSpins = cases.state.value.totalSpins,
            caseBonusPercent = cases.state.value.caseBonusPercent
        )
    )
}
