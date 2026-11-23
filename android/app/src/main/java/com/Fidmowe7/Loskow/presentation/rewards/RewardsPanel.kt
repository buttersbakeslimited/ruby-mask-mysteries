package com.Fidmowe7.Loskow.presentation.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.SurfaceDeep
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.EmptyState
import com.Fidmowe7.Loskow.core.ui.PrimaryButton
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.domain.model.RewardDay
import com.Fidmowe7.Loskow.domain.model.RewardState

@Composable
fun RewardsPanel(onMenu: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by rewardsViewModel.uiState.collectAsStateWithLifecycle()

    PanelScaffold(titleRes = R.string.route_rewards, onMenu = onMenu) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.rewards_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            if (state.streakBroken) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.rewards_streak_cold),
                    style = MaterialTheme.typography.labelSmall,
                    color = Ruby,
                    textAlign = TextAlign.Center
                )
            }
            if (state.clockSkew) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.rewards_clock_skew),
                    style = MaterialTheme.typography.labelSmall,
                    color = Ruby,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            if (state.ladder.isEmpty()) {
                EmptyState(messageRes = R.string.rewards_empty, iconRes = R.drawable.ic_rewards)
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (day in state.ladder.take(4)) {
                            RewardTile(day = day, modifier = Modifier.weight(1f))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (day in state.ladder.drop(4)) {
                            RewardTile(day = day, modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            if (state.claimable) {
                PrimaryButton(
                    textRes = R.string.cta_claim_today,
                    onClick = { rewardsViewModel.claim() },
                    iconRes = R.drawable.ic_rewards,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    textRes = R.string.cta_come_back,
                    onClick = { rewardsViewModel.claim() },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.rewards_hours_left, state.hoursLeft),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
private fun RewardTile(day: RewardDay, modifier: Modifier = Modifier) {
    val label = when (day.state) {
        RewardState.Claimed -> stringResource(R.string.status_claimed_short)
        RewardState.Ready -> stringResource(R.string.status_ready)
        RewardState.Locked -> stringResource(R.string.status_waiting)
    }
    val tint = when (day.state) {
        RewardState.Ready -> Ruby
        RewardState.Claimed -> GoldLight
        RewardState.Locked -> TextMuted
    }
    Column(
        modifier = modifier
            .background(
                if (day.state == RewardState.Ready) VelvetBrown else SurfaceRaised,
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                tint.copy(alpha = if (day.state == RewardState.Ready) 0.8f else 0.25f),
                RoundedCornerShape(12.dp)
            )
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .semantics { stateDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.rewards_day, day.day),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(SurfaceDeep, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(spriteRes(4)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (day.credits > 0) {
                stringResource(R.string.rewards_credits, day.credits)
            } else {
                stringResource(R.string.rewards_shards, day.shards)
            },
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            color = Gold,
            textAlign = TextAlign.Center
        )
    }
}
