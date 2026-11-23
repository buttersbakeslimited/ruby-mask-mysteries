package com.Fidmowe7.Loskow.domain.repository

import com.Fidmowe7.Loskow.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>
    fun update(transform: (GameSettings) -> GameSettings)
    fun reset()
}
