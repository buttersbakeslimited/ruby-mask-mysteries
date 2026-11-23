package com.Fidmowe7.Loskow.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.EmptyState
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.core.util.formatAmount
import com.Fidmowe7.Loskow.data.sample.SampleData
import com.Fidmowe7.Loskow.domain.model.CaseFolder
import com.Fidmowe7.Loskow.domain.model.CaseTile

@Composable
fun CollectionPanel(onMenu: () -> Unit) {
    val collectionViewModel: CollectionViewModel = viewModel(factory = ServiceLocator.factory)
    val state by collectionViewModel.uiState.collectAsStateWithLifecycle()
    val anyUnlocked = state.folders.any { folder -> folder.tiles.any { it.unlocked } }

    PanelScaffold(titleRes = R.string.route_collection, onMenu = onMenu) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.collection_shards, formatAmount(state.shards)),
                style = MaterialTheme.typography.labelMedium,
                color = Gold
            )
            Text(
                text = stringResource(R.string.collection_case_bonus, state.caseBonusPercent),
                style = MaterialTheme.typography.labelMedium,
                color = GoldLight
            )
        }

        if (state.shortOfShards) {
            Text(
                text = stringResource(R.string.collection_not_enough),
                style = MaterialTheme.typography.labelSmall,
                color = Ruby,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            )
        }

        if (!anyUnlocked && state.shards < 4) {
            EmptyState(messageRes = R.string.collection_empty, iconRes = R.drawable.ic_collection)
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.folders.size) { index ->
                CaseFolderBlock(
                    folder = state.folders[index],
                    onUnlock = { tile -> collectionViewModel.unlock(tile) }
                )
            }
        }
    }
}

@Composable
private fun CaseFolderBlock(folder: CaseFolder, onUnlock: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(SampleData.caseNames[folder.index]),
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
            if (folder.complete) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = GoldLight,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (tile in folder.tiles) {
                CollectionTileCell(
                    tile = tile,
                    onUnlock = onUnlock,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CollectionTileCell(
    tile: CaseTile,
    onUnlock: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (tile.unlocked) {
        stringResource(R.string.status_unlocked)
    } else {
        stringResource(R.string.collection_cost, tile.cost)
    }
    Box(
        modifier = modifier
            .aspectRatio(0.82f)
            .background(
                if (tile.unlocked) VelvetBrown else SurfaceRaised,
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                Gold.copy(alpha = if (tile.unlocked) 0.55f else 0.18f),
                RoundedCornerShape(14.dp)
            )
            .clickable(enabled = !tile.unlocked) { onUnlock(tile.index) }
            .semantics { stateDescription = label },
        contentAlignment = Alignment.Center
    ) {
        if (tile.unlocked) {
            Image(
                painter = painterResource(spriteRes(tile.spriteIndex)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(6.dp)
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    tint = Gold.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
