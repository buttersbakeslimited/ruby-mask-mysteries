package com.Fidmowe7.Loskow.presentation.gameover

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Fidmowe7.Loskow.core.theme.GoldLight
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextFaint
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.LeaderboardStrip
import com.Fidmowe7.Loskow.core.ui.PrimaryButton
import com.Fidmowe7.Loskow.core.ui.SecondaryButton
import com.Fidmowe7.Loskow.core.ui.StatEntry
import com.Fidmowe7.Loskow.core.ui.StatRow
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.core.util.formatAmount

@Composable
fun GameOverScreen(
    summary: Screen.GameOver,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.factory)
    val state by gameOverViewModel.uiState.collectAsStateWithLifecycle()

    val motion = rememberInfiniteTransition(label = "dossier")
    val sway by motion.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )

    val entries = ArrayList<StatEntry>()
    if (summary.sessionTotal > 0) {
        entries.add(
            StatEntry(R.string.stat_session_total, formatAmount(summary.sessionTotal), Gold)
        )
    }
    if (summary.bestWin > 0) {
        entries.add(StatEntry(R.string.stat_best_win, formatAmount(summary.bestWin), GoldLight))
    }
    if (summary.spins > 0) {
        entries.add(StatEntry(R.string.stat_spins, formatAmount(summary.spins), TextPrimary))
    }
    if (summary.shards > 0) {
        entries.add(StatEntry(R.string.stat_shards, formatAmount(summary.shards), Ruby))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(VelvetBrown, LacquerBlack)))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(Ruby.copy(alpha = 0.22f), Color.Transparent)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .border(1.dp, Gold.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(58.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    if (summary.outOfCredits) {
                        R.string.over_title_empty
                    } else {
                        R.string.over_title_closed
                    }
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(spriteRes(1)),
                contentDescription = stringResource(R.string.symbol_mask),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(104.dp)
                    .graphicsLayer { rotationZ = sway }
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (entries.size >= 2) {
                StatRow(entries = entries.take(2))
                if (entries.size >= 4) {
                    Spacer(modifier = Modifier.height(10.dp))
                    StatRow(entries = entries.drop(2))
                }
            } else {
                Text(
                    text = stringResource(R.string.over_no_stats),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LeaderboardStrip(rows = state.board)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.over_rank, state.playerRank),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )

            if (state.needsRefill) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.over_refill, GameConfig.REFILL_CREDITS),
                    style = MaterialTheme.typography.labelMedium,
                    color = GoldLight,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                textRes = R.string.cta_spin_again,
                onClick = {
                    gameOverViewModel.grantRefill()
                    onPlayAgain()
                },
                iconRes = R.drawable.ic_spin,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                textRes = R.string.cta_menu,
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = stringResource(R.string.over_disclaimer),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = TextFaint,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        )
    }
}
