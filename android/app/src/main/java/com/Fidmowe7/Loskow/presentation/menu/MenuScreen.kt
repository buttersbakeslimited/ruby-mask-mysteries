package com.Fidmowe7.Loskow.presentation.menu

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.navigation.Screen
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.SurfaceDeep
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.ui.LeaderboardStrip
import com.Fidmowe7.Loskow.core.ui.PrimaryButton
import com.Fidmowe7.Loskow.core.util.formatAmount

private data class MenuTile(
    val labelRes: Int,
    val iconRes: Int,
    val target: Screen,
    val flagged: Boolean
)

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onRoute: (Screen) -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory)
    val state by menuViewModel.uiState.collectAsStateWithLifecycle()

    val badgePulse = remember { Animatable(1f) }
    LaunchedEffect(state.credits) {
        badgePulse.animateTo(1.06f, tween(130))
        badgePulse.animateTo(1f, tween(130))
    }

    val tiles = listOf(
        MenuTile(R.string.route_lobby, R.drawable.ic_lobby, Screen.Lobby, false),
        MenuTile(R.string.route_collection, R.drawable.ic_collection, Screen.Collection, state.collectionReady),
        MenuTile(R.string.route_missions, R.drawable.ic_missions, Screen.Missions, state.missionReady),
        MenuTile(R.string.route_rewards, R.drawable.ic_rewards, Screen.Rewards, state.rewardReady),
        MenuTile(R.string.route_settings, R.drawable.ic_settings, Screen.Settings, false),
        MenuTile(R.string.route_responsible, R.drawable.ic_shield, Screen.ResponsibleGaming, false)
    )

    Box(modifier = Modifier.fillMaxSize().background(LacquerBlack)) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Transparent,
                            0.34f to LacquerBlack.copy(alpha = 0.55f),
                            0.5f to LacquerBlack
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = SurfaceRaised.copy(alpha = 0.86f),
                    border = BorderStroke(1.dp, Gold.copy(alpha = 0.45f))
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_home),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.padding(6.dp).size(24.dp)
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = SurfaceRaised.copy(alpha = 0.86f),
                    border = BorderStroke(1.dp, Gold.copy(alpha = 0.45f)),
                    modifier = Modifier.graphicsLayer {
                        scaleX = badgePulse.value
                        scaleY = badgePulse.value
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_gem),
                            contentDescription = null,
                            tint = Gold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = stringResource(R.string.badge_credits, formatAmount(state.credits)),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = SurfaceDeep,
                shadowElevation = 18.dp,
                border = BorderStroke(1.dp, Gold.copy(alpha = 0.22f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(listOf(SurfaceRaised, SurfaceDeep))
                        )
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp, bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(Gold.copy(alpha = 0.6f), CircleShape)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.app_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LeaderboardStrip(rows = state.board)
                    Spacer(modifier = Modifier.height(14.dp))
                    TileGrid(tiles = tiles, onRoute = onRoute)
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        textRes = R.string.cta_play,
                        onClick = onPlay,
                        iconRes = R.drawable.ic_play,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TileGrid(tiles: List<MenuTile>, onRoute: (Screen) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (rowIndex in 0 until (tiles.size + 1) / 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (columnIndex in 0 until 2) {
                    val index = rowIndex * 2 + columnIndex
                    if (index < tiles.size) {
                        MenuTileButton(
                            tile = tiles[index],
                            onRoute = onRoute,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuTileButton(
    tile: MenuTile,
    onRoute: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { onRoute(tile.target) },
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Gold.copy(alpha = 0.28f)),
            contentPadding = PaddingValues(horizontal = 10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SurfaceRaised.copy(alpha = 0.7f),
                contentColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                painter = painterResource(tile.iconRes),
                contentDescription = null,
                tint = Gold,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(tile.labelRes),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
        if (tile.flagged) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Ruby)
            )
        }
    }
}
