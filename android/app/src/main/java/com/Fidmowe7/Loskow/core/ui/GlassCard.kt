package com.Fidmowe7.Loskow.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 18.dp,
    borderAlpha: Float = 0.22f,
    tint: Color = SurfaceRaised,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Surface(
        shape = shape,
        color = tint.copy(alpha = 0.78f),
        border = BorderStroke(1.dp, Gold.copy(alpha = borderAlpha)),
        shadowElevation = 10.dp,
        modifier = modifier
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Ruby.copy(alpha = 0.08f), Color.Transparent)
                        )
                    )
            )
            content()
        }
    }
}
