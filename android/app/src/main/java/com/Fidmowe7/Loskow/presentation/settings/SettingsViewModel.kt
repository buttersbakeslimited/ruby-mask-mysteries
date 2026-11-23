package com.Fidmowe7.Loskow.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.domain.model.GameSettings
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class SettingsUiState(val options: GameSettings)

class SettingsViewModel(
    private val cases: CaseRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settings.settings
        .map { SettingsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsUiState(settings.settings.value)
        )

    fun setSpeed(index: Int) {
        settings.update { it.copy(speedIndex = index.coerceIn(0, 2)) }
    }

    fun setBreak(index: Int) {
        settings.update { it.copy(breakIndex = index.coerceIn(0, 2)) }
    }

    fun toggleNearWin(enabled: Boolean) {
        settings.update { it.copy(nearWinHighlight = enabled) }
    }

    fun toggleContrast(enabled: Boolean) {
        settings.update { it.copy(highContrast = enabled) }
    }

    fun resetCaseFile() {
        cases.reset()
        settings.reset()
    }
}
