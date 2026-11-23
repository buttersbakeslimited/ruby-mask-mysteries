package com.Fidmowe7.Loskow.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.repository.SettingsRepository
import com.Fidmowe7.Loskow.presentation.app.AppViewModel
import com.Fidmowe7.Loskow.presentation.bonus.BonusViewModel
import com.Fidmowe7.Loskow.presentation.collection.CollectionViewModel
import com.Fidmowe7.Loskow.presentation.game.GameViewModel
import com.Fidmowe7.Loskow.presentation.gameover.GameOverViewModel
import com.Fidmowe7.Loskow.presentation.lobby.LobbyViewModel
import com.Fidmowe7.Loskow.presentation.menu.MenuViewModel
import com.Fidmowe7.Loskow.presentation.missions.MissionsViewModel
import com.Fidmowe7.Loskow.presentation.responsible.ResponsibleGamingViewModel
import com.Fidmowe7.Loskow.presentation.rewards.RewardsViewModel
import com.Fidmowe7.Loskow.presentation.settings.SettingsViewModel
import com.Fidmowe7.Loskow.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val cases: CaseRepository,
    private val settings: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(cases)
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(cases, settings)
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(cases)
            modelClass.isAssignableFrom(LobbyViewModel::class.java) -> LobbyViewModel(cases)
            modelClass.isAssignableFrom(BonusViewModel::class.java) -> BonusViewModel(cases)
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> CollectionViewModel(cases)
            modelClass.isAssignableFrom(MissionsViewModel::class.java) -> MissionsViewModel(cases)
            modelClass.isAssignableFrom(RewardsViewModel::class.java) -> RewardsViewModel(cases)
            modelClass.isAssignableFrom(ResponsibleGamingViewModel::class.java) ->
                ResponsibleGamingViewModel(cases, settings)
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(cases, settings)
            else -> throw IllegalArgumentException(modelClass.name)
        }
        @Suppress("UNCHECKED_CAST")
        return created as T
    }
}
