package com.Fidmowe7.Loskow.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.navigation.Screen
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.ui.PrimaryButton
import com.Fidmowe7.Loskow.core.ui.ScreenBackground
import com.Fidmowe7.Loskow.core.ui.SecondaryButton
import com.Fidmowe7.Loskow.core.ui.StatEntry
import com.Fidmowe7.Loskow.core.ui.StatRow
import com.Fidmowe7.Loskow.core.util.formatAmount
import com.Fidmowe7.Loskow.presentation.dialog.BreakReminderDialog
import com.Fidmowe7.Loskow.presentation.dialog.PauseDialog

@Composable
fun GameScreen(
    onHome: () -> Unit,
    onBonus: () -> Unit,
    onResponsible: () -> Unit,
    onSessionEnd: (Screen.GameOver) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameViewModel.uiState.collectAsStateWithLifecycle()
    var pauseOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.OpenBonus -> onBonus()
                is GameEvent.SessionEnded -> onSessionEnd(event.summary)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    ScreenBackground(
        overlay = listOf(
            LacquerBlack.copy(alpha = 0.88f),
            LacquerBlack.copy(alpha = 0.70f),
            LacquerBlack.copy(alpha = 0.94f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                mapNode = state.mapNode,
                onHome = onHome,
                onPause = { pauseOpen = true }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MaskMeter(
                    charge = state.meter,
                    maximum = GameConfig.METER_MAX
                )
                Spacer(modifier = Modifier.height(14.dp))
                ReelCabinet(
                    grid = state.grid,
                    stoppedReels = state.stoppedReels,
                    winningLines = state.winningLines,
                    highContrast = state.highContrast,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.nearWinVisible) {
                        Text(
                            text = stringResource(R.string.game_near_win),
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 12.sp,
                            color = Ruby,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            StatRow(
                entries = statEntries(state),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    textRes = R.string.cta_spin,
                    onClick = { gameViewModel.spin() },
                    enabled = state.canSpin,
                    height = 64.dp,
                    iconRes = R.drawable.ic_spin,
                    stateLabel = stringResource(
                        if (state.phase == SpinPhase.Idle) {
                            R.string.state_ready
                        } else {
                            R.string.state_spinning
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    textRes = R.string.cta_end_session,
                    onClick = { gameViewModel.endSession() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (pauseOpen) {
        PauseDialog(
            onResume = { pauseOpen = false },
            onResponsible = {
                pauseOpen = false
                onResponsible()
            },
            onMenu = {
                pauseOpen = false
                onHome()
            }
        )
    }

    if (state.breakBannerVisible) {
        BreakReminderDialog(
            spins = state.sessionSpins,
            onDismiss = { gameViewModel.dismissBreakBanner() },
            onResponsible = {
                gameViewModel.dismissBreakBanner()
                onResponsible()
            }
        )
    }
}

private fun statEntries(state: GameUiState): List<StatEntry> {
    val entries = ArrayList<StatEntry>()
    entries.add(StatEntry(R.string.stat_credits, formatAmount(state.credits), Gold))
    entries.add(StatEntry(R.string.stat_bet, formatAmount(state.bet), TextPrimary))
    if (state.lastWin > 0) {
        entries.add(StatEntry(R.string.stat_last_win, formatAmount(state.lastWin), Ruby))
    }
    return entries
}

@Composable
private fun GameHeader(
    mapNode: Int,
    onHome: () -> Unit,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onHome,
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_back_home),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.game_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = stringResource(R.string.game_case_of, mapNode, GameConfig.MAP_NODES),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
        IconButton(
            onClick = onPause,
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(26.dp)
            )
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gold.copy(alpha = 0.2f))
    )
}
