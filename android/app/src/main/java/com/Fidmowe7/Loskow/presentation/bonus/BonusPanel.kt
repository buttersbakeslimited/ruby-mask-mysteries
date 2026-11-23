package com.Fidmowe7.Loskow.presentation.bonus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.GoldLight
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.SurfaceDeep
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.PrimaryButton
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.core.util.formatAmount
import com.Fidmowe7.Loskow.domain.model.VaultDoor
import com.Fidmowe7.Loskow.domain.model.VaultPrize
import com.Fidmowe7.Loskow.presentation.game.GameViewModel

@Composable
fun BonusPanel(onDone: () -> Unit) {
    val bonusViewModel: BonusViewModel = viewModel(factory = ServiceLocator.factory)
    val sessionViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by bonusViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(VelvetBrown, LacquerBlack)))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(listOf(Ruby.copy(alpha = 0.26f), Color.Transparent))
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.bonus_title),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.bonus_picks_left, state.picksLeft),
                style = MaterialTheme.typography.labelMedium,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.bonus_hint),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (column in 0 until 3) {
                            val index = row * 3 + column
                            val door = state.doors.getOrNull(index)
                            if (door != null) {
                                VaultDoorTile(
                                    door = door,
                                    onPick = { bonusViewModel.pick(index) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(
                    R.string.bonus_haul,
                    formatAmount(state.credits),
                    formatAmount(state.shards)
                ),
                style = MaterialTheme.typography.titleSmall,
                color = Gold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                textRes = R.string.cta_back_to_reels,
                onClick = {
                    sessionViewModel.applyVaultHaul(state.credits, state.shards, state.clues)
                    bonusViewModel.reset()
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun VaultDoorTile(
    door: VaultDoor,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val flip by animateFloatAsState(
        targetValue = if (door.revealed) 180f else 0f,
        animationSpec = tween(420),
        label = "doorFlip"
    )
    val prizeLabel = prizeText(door.prize)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = flip
                cameraDistance = 12f * density
            }
            .background(SurfaceDeep, RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (door.revealed) Gold.copy(alpha = 0.7f) else Gold.copy(alpha = 0.25f),
                RoundedCornerShape(14.dp)
            )
            .clickable(enabled = !door.revealed) { onPick() }
            .semantics { stateDescription = prizeLabel },
        contentAlignment = Alignment.Center
    ) {
        if (door.revealed) {
            Text(
                text = prizeLabel,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = GoldLight,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(4.dp)
                    .graphicsLayer { rotationY = 180f }
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(spriteRes(1)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(42.dp)
                )
                Text(
                    text = formatAmount(door.index + 1),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = Gold
                )
            }
        }
    }
}

@Composable
private fun prizeText(prize: VaultPrize): String = when (prize) {
    VaultPrize.CreditsSmall -> stringResource(R.string.vault_credits, prize.credits)
    VaultPrize.CreditsLarge -> stringResource(R.string.vault_credits, prize.credits)
    VaultPrize.ShardsSmall -> stringResource(R.string.vault_shards, prize.shards)
    VaultPrize.ShardsLarge -> stringResource(R.string.vault_shards, prize.shards)
    VaultPrize.CaseClue -> stringResource(R.string.vault_clue)
    VaultPrize.CollectAll -> stringResource(R.string.vault_collect_all)
}
