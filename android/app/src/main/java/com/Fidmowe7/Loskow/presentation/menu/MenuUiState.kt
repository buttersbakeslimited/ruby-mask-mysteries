package com.Fidmowe7.Loskow.presentation.menu

import com.Fidmowe7.Loskow.core.ui.BoardRow

data class MenuUiState(
    val credits: Int,
    val shards: Int,
    val board: List<BoardRow>,
    val rewardReady: Boolean,
    val missionReady: Boolean,
    val collectionReady: Boolean,
    val caseBonusPercent: Int
)
