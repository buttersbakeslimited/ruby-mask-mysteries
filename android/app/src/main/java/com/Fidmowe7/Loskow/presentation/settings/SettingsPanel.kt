package com.Fidmowe7.Loskow.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Fidmowe7.Loskow.core.theme.LacquerBlack
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.SurfaceRaised
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.ui.GlassCard
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.presentation.dialog.ConfirmDialog

@Composable
fun SettingsPanel(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.factory)
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()
    var confirmOpen by rememberSaveable { mutableStateOf(false) }

    val speedLabels = listOf(
        R.string.speed_relaxed,
        R.string.speed_standard,
        R.string.speed_swift
    )

    PanelScaffold(titleRes = R.string.route_settings, onMenu = onMenu) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SegmentedRow(
                    titleRes = R.string.setting_reel_speed,
                    labels = speedLabels,
                    selected = state.options.speedIndex,
                    onSelect = { settingsViewModel.setSpeed(it) },
                    detail = stringResource(
                        R.string.setting_speed_detail,
                        GameConfig.SPIN_MS[state.options.speedIndex.coerceIn(0, 2)],
                        GameConfig.STAGGER_MS[state.options.speedIndex.coerceIn(0, 2)]
                    )
                )
            }
            item {
                SwitchRow(
                    titleRes = R.string.setting_near_win,
                    detailRes = R.string.setting_near_win_detail,
                    checked = state.options.nearWinHighlight,
                    onCheckedChange = { settingsViewModel.toggleNearWin(it) }
                )
            }
            item {
                SwitchRow(
                    titleRes = R.string.setting_contrast,
                    detailRes = R.string.setting_contrast_detail,
                    checked = state.options.highContrast,
                    onCheckedChange = { settingsViewModel.toggleContrast(it) }
                )
            }
            item {
                SegmentedRow(
                    titleRes = R.string.setting_break,
                    labels = listOf(
                        R.string.break_fifty,
                        R.string.break_hundred,
                        R.string.break_two_hundred
                    ),
                    selected = state.options.breakIndex,
                    onSelect = { settingsViewModel.setBreak(it) },
                    detail = stringResource(
                        R.string.setting_break_detail,
                        GameConfig.BREAK_OPTIONS[state.options.breakIndex.coerceIn(0, 2)]
                    )
                )
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = stringResource(R.string.setting_reset),
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.setting_reset_detail),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { confirmOpen = true },
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Ruby.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = LacquerBlack.copy(alpha = 0.4f),
                                contentColor = Ruby
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.cta_reset_case),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }

    if (confirmOpen) {
        ConfirmDialog(
            titleRes = R.string.reset_title,
            bodyRes = R.string.reset_body,
            confirmRes = R.string.cta_reset_case,
            onConfirm = {
                confirmOpen = false
                settingsViewModel.resetCaseFile()
            },
            onCancel = { confirmOpen = false }
        )
    }
}

@Composable
private fun SegmentedRow(
    titleRes: Int,
    labels: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit,
    detail: String
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = detail }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (index in labels.indices) {
                    val active = index == selected
                    OutlinedButton(
                        onClick = { onSelect(index) },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            Gold.copy(alpha = if (active) 0.75f else 0.2f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (active) {
                                Gold.copy(alpha = 0.18f)
                            } else {
                                SurfaceRaised.copy(alpha = 0.5f)
                            },
                            contentColor = if (active) Gold else TextMuted
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 48.dp)
                    ) {
                        Text(
                            text = stringResource(labels[index]),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun SwitchRow(
    titleRes: Int,
    detailRes: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val label = stringResource(if (checked) R.string.state_on else R.string.state_off)
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = label }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(detailRes),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LacquerBlack,
                    checkedTrackColor = Gold,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = DisabledSurface
                )
            )
        }
    }
}
