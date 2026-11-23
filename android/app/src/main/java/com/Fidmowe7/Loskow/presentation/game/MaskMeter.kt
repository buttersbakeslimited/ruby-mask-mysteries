package com.Fidmowe7.Loskow.presentation.game

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.theme.DisabledSurface
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextMuted

@Composable
fun MaskMeter(
    charge: Int,
    maximum: Int,
    modifier: Modifier = Modifier
) {
    val ready = charge >= maximum
    val breath = if (ready) {
        val motion = rememberInfiniteTransition(label = "meterBreath")
        val value by motion.animateFloat(
            initialValue = 0.6f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(900),
                repeatMode = RepeatMode.Reverse
            ),
            label = "breath"
        )
        value
    } else {
        1f
    }
    val label = if (ready) {
        stringResource(R.string.meter_ready)
    } else {
        stringResource(R.string.meter_label, charge, maximum)
    }
    Column(
        modifier = modifier.semantics { stateDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            for (notch in 0 until maximum) {
                val filled = notch < charge
                Spacer(
                    modifier = Modifier
                        .width(26.dp)
                        .height(8.dp)
                        .alpha(if (filled) breath else 1f)
                        .background(
                            if (filled) Ruby else DisabledSurface,
                            RoundedCornerShape(4.dp)
                        )
                        .border(
                            1.dp,
                            Gold.copy(alpha = if (filled) 0.7f else 0.25f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = if (ready) Gold else TextMuted,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}
