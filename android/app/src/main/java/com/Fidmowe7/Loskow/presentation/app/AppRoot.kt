package com.Fidmowe7.Loskow.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.navigation.Screen
import com.Fidmowe7.Loskow.presentation.bonus.BonusPanel
import com.Fidmowe7.Loskow.presentation.collection.CollectionPanel
import com.Fidmowe7.Loskow.presentation.game.GameScreen
import com.Fidmowe7.Loskow.presentation.game.GameViewModel
import com.Fidmowe7.Loskow.presentation.gameover.GameOverScreen
import com.Fidmowe7.Loskow.presentation.lobby.LobbyPanel
import com.Fidmowe7.Loskow.presentation.menu.MenuScreen
import com.Fidmowe7.Loskow.presentation.missions.MissionsPanel
import com.Fidmowe7.Loskow.presentation.responsible.ResponsibleGamingPanel
import com.Fidmowe7.Loskow.presentation.rewards.RewardsPanel
import com.Fidmowe7.Loskow.presentation.settings.SettingsPanel
import com.Fidmowe7.Loskow.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory)
    val sessionViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    val goHome = { appViewModel.resetTo(Screen.Menu) }
    val goBack = {
        if (!appViewModel.back()) {
            appViewModel.resetTo(Screen.Menu)
        }
    }

    BackHandler(enabled = screen != Screen.Menu && screen != Screen.Splash) { goBack() }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            fadeIn(animationSpec = tween(420)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "screenHost",
        modifier = Modifier.fillMaxSize()
    ) { current ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (current) {
                is Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.replace(Screen.Menu) }
                )

                is Screen.Menu -> MenuScreen(
                    onPlay = {
                        sessionViewModel.restartSession()
                        appViewModel.navigate(Screen.Game)
                    },
                    onRoute = { target -> appViewModel.navigate(target) }
                )

                is Screen.Lobby -> LobbyPanel(
                    onMenu = goHome,
                    onEnterReels = {
                        sessionViewModel.restartSession()
                        appViewModel.navigate(Screen.Game)
                    }
                )

                is Screen.Game -> GameScreen(
                    onHome = goHome,
                    onBonus = { appViewModel.navigate(Screen.Bonus) },
                    onResponsible = { appViewModel.navigate(Screen.ResponsibleGaming) },
                    onSessionEnd = { summary -> appViewModel.replace(summary) }
                )

                is Screen.Bonus -> BonusPanel(onDone = goBack)

                is Screen.GameOver -> GameOverScreen(
                    summary = current,
                    onPlayAgain = {
                        sessionViewModel.restartSession()
                        appViewModel.replace(Screen.Game)
                    },
                    onMenu = goHome
                )

                is Screen.Collection -> CollectionPanel(onMenu = goHome)

                is Screen.Missions -> MissionsPanel(onMenu = goHome)

                is Screen.Rewards -> RewardsPanel(onMenu = goHome)

                is Screen.ResponsibleGaming -> ResponsibleGamingPanel(
                    onMenu = goHome,
                    onTakeBreak = {
                        sessionViewModel.resetSessionCounter()
                        appViewModel.resetTo(Screen.Menu)
                    }
                )

                is Screen.Settings -> SettingsPanel(onMenu = goHome)
            }
        }
    }
}
