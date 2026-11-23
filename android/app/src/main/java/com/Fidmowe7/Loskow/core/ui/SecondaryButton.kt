package com.Fidmowe7.Loskow.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Fidmowe7.Loskow.core.theme.DisabledText
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised
import com.Fidmowe7.Loskow.core.theme.TextPrimary

@Composable
fun SecondaryButton(
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    iconRes: Int? = null
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, if (enabled) Gold.copy(alpha = 0.34f) else Color(0xFF4A4438)),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SurfaceRaised.copy(alpha = 0.72f),
            contentColor = TextPrimary,
            disabledContainerColor = SurfaceRaised.copy(alpha = 0.4f),
            disabledContentColor = DisabledText
        ),
        modifier = modifier
            .height(height)
            .defaultMinSize(minHeight = 48.dp)
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (enabled) Gold else DisabledText,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}
