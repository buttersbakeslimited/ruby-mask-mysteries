package com.Fidmowe7.Loskow.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Lobby : Screen
    data object Game : Screen
    data object Bonus : Screen
    data class GameOver(
        val sessionTotal: Int,
        val bestWin: Int,
        val spins: Int,
        val shards: Int,
        val outOfCredits: Boolean
    ) : Screen
    data object Collection : Screen
    data object Missions : Screen
    data object Rewards : Screen
    data object ResponsibleGaming : Screen
    data object Settings : Screen
}
