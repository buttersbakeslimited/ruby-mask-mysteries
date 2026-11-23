package com.Fidmowe7.Loskow.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.GameSettings

class SettingsPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ruby_mask_settings", Context.MODE_PRIVATE)

    fun read(): GameSettings = GameSettings(
        speedIndex = prefs.getInt(KEY_SPEED, GameConfig.DEFAULT_SPEED_INDEX),
        nearWinHighlight = prefs.getBoolean(KEY_NEAR_WIN, true),
        highContrast = prefs.getBoolean(KEY_CONTRAST, false),
        breakIndex = prefs.getInt(KEY_BREAK, GameConfig.DEFAULT_BREAK_INDEX)
    )

    fun write(settings: GameSettings) {
        prefs.edit()
            .putInt(KEY_SPEED, settings.speedIndex)
            .putBoolean(KEY_NEAR_WIN, settings.nearWinHighlight)
            .putBoolean(KEY_CONTRAST, settings.highContrast)
            .putInt(KEY_BREAK, settings.breakIndex)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val KEY_SPEED = "reel_speed"
        const val KEY_NEAR_WIN = "near_win_highlight"
        const val KEY_CONTRAST = "high_contrast"
        const val KEY_BREAK = "break_reminder_spins"
    }
}
