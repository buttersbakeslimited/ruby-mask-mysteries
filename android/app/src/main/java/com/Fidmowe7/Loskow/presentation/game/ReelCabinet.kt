package com.Fidmowe7.Loskow.presentation.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.core.theme.BezelShadow
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.GoldLight
import com.Fidmowe7.Loskow.core.theme.LacquerVoid
import com.Fidmowe7.Loskow.core.theme.SurfaceDeep
import com.Fidmowe7.Loskow.core.theme.VelvetBrown
import com.Fidmowe7.Loskow.core.ui.spriteRes
import com.Fidmowe7.Loskow.core.ui.symbolNameRes
import com.Fidmowe7.Loskow.domain.model.ReelSymbol
import kotlin.math.floor

private const val BOARD_PAD = 8
private const val BOARD_BORDER = 3

@Composable
fun ReelCabinet(
    grid: List<ReelSymbol>,
    stoppedReels: Int,
    winningLines: Set<Int>,
    highContrast: Boolean,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val available = minOf(maxWidth - 32.dp, 380.dp)
        val frame = (BOARD_PAD + BOARD_BORDER).dp
        val cell = Dp(floor((available - frame * 2).value / GameConfig.REELS))
        val board = cell * GameConfig.REELS + frame * 2

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = VelvetBrown,
            shadowElevation = 16.dp,
            border = BorderStroke(
                BOARD_BORDER.dp,
                Brush.linearGradient(listOf(GoldLight, BezelShadow, Gold))
            ),
            modifier = Modifier
                .width(board)
                .height(board)
        ) {
            Box(
                modifier = Modifier
                    .padding(BOARD_PAD.dp)
                    .border(1.dp, LacquerVoid.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.Top) {
                    for (row in 0 until GameConfig.ROWS) {
                        ReelRow(
                            row = row,
                            grid = grid,
                            cell = cell,
                            stoppedReels = stoppedReels,
                            winning = winningLines.contains(row),
                            highContrast = highContrast
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReelRow(
    row: Int,
    grid: List<ReelSymbol>,
    cell: Dp,
    stoppedReels: Int,
    winning: Boolean,
    highContrast: Boolean
) {
    val railColour = if (winning) GoldLight else Gold.copy(alpha = 0.18f)
    Box(
        modifier = Modifier
            .width(cell * GameConfig.REELS)
            .height(cell)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(2.dp)
                .background(railColour)
        )
        Row {
            for (column in 0 until GameConfig.REELS) {
                ReelCell(
                    symbol = grid[row * GameConfig.REELS + column],
                    cell = cell,
                    spinning = column >= stoppedReels,
                    winning = winning,
                    highContrast = highContrast
                )
            }
        }
    }
}

@Composable
private fun ReelCell(
    symbol: ReelSymbol,
    cell: Dp,
    spinning: Boolean,
    winning: Boolean,
    highContrast: Boolean
) {
    val roll = if (spinning) {
        val motion = rememberInfiniteTransition(label = "cellRoll")
        val value by motion.animateFloat(
            initialValue = -0.5f,
            targetValue = 0.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(180, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "roll"
        )
        value
    } else {
        0f
    }
    val borderAlpha = when {
        winning -> 0.85f
        highContrast -> 0.55f
        else -> 0.14f
    }
    Box(
        modifier = Modifier
            .size(cell)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (highContrast) SurfaceDeep.copy(alpha = 1f) else SurfaceDeep,
            border = BorderStroke(1.dp, Gold.copy(alpha = borderAlpha)),
            modifier = Modifier.size(cell - 8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (highContrast) {
                    Box(
                        modifier = Modifier
                            .size(cell - 8.dp)
                            .background(Color.White.copy(alpha = 0.06f))
                    )
                }
                Image(
                    painter = painterResource(spriteRes(symbol.spriteIndex)),
                    contentDescription = stringResource(symbolNameRes(symbol)),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(cell - 34.dp)
                        .graphicsLayer {
                            translationY = roll * size.height
                            alpha = if (spinning) 0.82f else 1f
                        }
                )
            }
        }
    }
}
