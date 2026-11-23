package com.Fidmowe7.Loskow.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Fidmowe7.Loskow.core.config.GameConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashUiState(val finished: Boolean = false)

class SplashViewModel : ViewModel() {

    private val backing = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = backing.asStateFlow()

    init {
        viewModelScope.launch {
            delay(GameConfig.LOADER_DURATION_MS)
            backing.update { it.copy(finished = true) }
        }
    }
}
