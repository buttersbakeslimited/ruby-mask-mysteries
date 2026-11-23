package com.Fidmowe7.Loskow.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.VelvetBrown

@Composable
fun PanelScaffold(
    titleRes: Int,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            VelvetBrown.copy(alpha = 0.92f),
            LacquerBlack.copy(alpha = 0.95f),
            LacquerBlack.copy(alpha = 0.98f)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            SectionHeader(
                titleRes = titleRes,
                trailing = {
                    SecondaryButton(
                        textRes = R.string.cta_menu,
                        onClick = onMenu,
                        height = 48.dp
                    )
                }
            )
            content()
        }
    }
}
