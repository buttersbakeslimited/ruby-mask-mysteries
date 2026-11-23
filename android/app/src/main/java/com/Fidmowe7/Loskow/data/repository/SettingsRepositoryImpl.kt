package com.Fidmowe7.Loskow.data.repository

import com.Fidmowe7.Loskow.data.local.SettingsPrefs
import com.Fidmowe7.Loskow.domain.model.GameSettings
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val prefs: SettingsPrefs) : SettingsRepository {

    private val backing = MutableStateFlow(prefs.read())

    override val settings: StateFlow<GameSettings> = backing.asStateFlow()

    override fun update(transform: (GameSettings) -> GameSettings) {
        val next = transform(backing.value)
        backing.value = next
        prefs.write(next)
    }

    override fun reset() {
        prefs.clear()
        backing.value = prefs.read()
    }
}
