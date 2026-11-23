package com.Fidmowe7.Loskow.presentation.lobby

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.GoldLight
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.ui.EmptyState
import com.Fidmowe7.Loskow.core.ui.GlassCard
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.core.ui.SecondaryButton
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.FeaturePreview
import com.Fidmowe7.Loskow.domain.model.PreviewStatus

@Composable
fun LobbyPanel(
    onMenu: () -> Unit,
    onEnterReels: () -> Unit
) {
    val lobbyViewModel: LobbyViewModel = viewModel(factory = ServiceLocator.factory)
    val state by lobbyViewModel.uiState.collectAsStateWithLifecycle()

    PanelScaffold(titleRes = R.string.route_lobby, onMenu = onMenu) {
        if (state.previews.isEmpty()) {
            EmptyState(messageRes = R.string.lobby_empty)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 6.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.previews) { preview ->
                    PreviewCard(preview = preview, onEnterReels = onEnterReels)
                }
            }
        }
    }
}

@Composable
private fun PreviewCard(preview: FeaturePreview, onEnterReels: () -> Unit) {
    val locked = preview.status == PreviewStatus.Locked
    val statusRes = when (preview.status) {
        PreviewStatus.Unlocked -> R.string.status_unlocked
        PreviewStatus.Locked -> R.string.status_locked
        PreviewStatus.Ready -> R.string.status_ready
        PreviewStatus.Claimed -> R.string.status_claimed
    }
    val statusColour = when (preview.status) {
        PreviewStatus.Ready -> Ruby
        PreviewStatus.Locked -> TextMuted
        else -> GoldLight
    }
    val statusLabel = stringResource(statusRes)
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = statusLabel },
        borderAlpha = if (locked) 0.1f else 0.24f
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .alpha(if (locked) 0.6f else 1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Gold.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                    .border(1.dp, Gold.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(spriteRes(preview.spriteIndex)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(SampleData.previewTitles[preview.index]),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(SampleData.previewRules[preview.index]),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = statusColour
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            SecondaryButton(
                textRes = R.string.cta_enter_reels,
                onClick = onEnterReels,
                enabled = !locked
            )
        }
    }
}
