package com.Fidmowe7.Loskow.presentation.missions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.theme.DisabledSurface
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.GoldLight
import com.Fidmowe7.Loskow.core.theme.SurfaceDeep
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.EmptyState
import com.Fidmowe7.Loskow.core.ui.GlassCard
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.core.ui.SecondaryButton
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.Mission

@Composable
fun MissionsPanel(onMenu: () -> Unit) {
    val missionsViewModel: MissionsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by missionsViewModel.uiState.collectAsStateWithLifecycle()

    PanelScaffold(titleRes = R.string.route_missions, onMenu = onMenu) {
        CaseMap(mapNode = state.mapNode)
        if (state.allClaimed) {
            EmptyState(messageRes = R.string.missions_empty, iconRes = R.drawable.ic_missions)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.missions.size) { index ->
                    MissionRow(
                        mission = state.missions[index],
                        onClaim = { missionsViewModel.claim(state.missions[index].id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CaseMap(mapNode: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (node in 1..GameConfig.MAP_NODES) {
            val reached = node <= mapNode
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (reached) VelvetBrown else SurfaceDeep,
                        CircleShape
                    )
                    .border(
                        1.dp,
                        Gold.copy(alpha = if (reached) 0.8f else 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (reached) {
                    Image(
                        painter = painterResource(spriteRes(5)),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            if (node < GameConfig.MAP_NODES) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(Gold.copy(alpha = 0.22f))
                )
            }
        }
    }
    Text(
        text = stringResource(SampleData.mapNodeNames[(mapNode - 1).coerceIn(0, 7)]),
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        color = GoldLight,
        modifier = Modifier.padding(horizontal = 18.dp)
    )
}

@Composable
private fun MissionRow(mission: Mission, onClaim: () -> Unit) {
    val status = when {
        mission.claimed -> stringResource(R.string.status_claimed_short)
        mission.complete -> stringResource(R.string.status_ready)
        else -> stringResource(R.string.mission_progress, mission.progress, mission.target)
    }
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = status },
        corner = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(SampleData.missionTitles.getValue(mission.id)),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 13.sp,
                    color = if (mission.claimed) TextMuted else TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { mission.ratio },
                    color = if (mission.complete) GoldLight else Gold,
                    trackColor = DisabledSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (mission.claimable) {
                SecondaryButton(
                    textRes = R.string.cta_claim,
                    onClick = onClaim
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(86.dp)
                        .height(48.dp)
                        .background(SurfaceDeep.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.mission_reward, mission.id.shardReward),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
