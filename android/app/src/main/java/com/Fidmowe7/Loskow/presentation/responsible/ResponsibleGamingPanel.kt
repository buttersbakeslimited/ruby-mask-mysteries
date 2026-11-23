package com.Fidmowe7.Loskow.presentation.responsible

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.di.ServiceLocator
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.ui.GlassCard
import com.Fidmowe7.Loskow.core.ui.PanelScaffold
import com.Fidmowe7.Loskow.core.ui.SecondaryButton
import com.Fidmowe7.Loskow.presentation.game.GameViewModel

@Composable
fun ResponsibleGamingPanel(
    onMenu: () -> Unit,
    onTakeBreak: () -> Unit
) {
    val responsibleViewModel: ResponsibleGamingViewModel =
        viewModel(factory = ServiceLocator.factory)
    val sessionViewModel: GameViewModel = viewModel(factory = ServiceLocator.factory)
    val state by responsibleViewModel.uiState.collectAsStateWithLifecycle()
    val session by sessionViewModel.uiState.collectAsStateWithLifecycle()

    PanelScaffold(titleRes = R.string.route_responsible, onMenu = onMenu) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                InfoCard(
                    titleRes = R.string.responsible_age_title,
                    bodyRes = R.string.responsible_age_body
                )
            }
            item {
                InfoCard(
                    titleRes = R.string.responsible_odds_title,
                    bodyRes = R.string.responsible_odds_body
                )
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = stringResource(R.string.responsible_breaks_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.responsible_session_spins,
                                session.sessionSpins
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = stringResource(
                                R.string.responsible_total_spins,
                                state.totalSpins
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = stringResource(
                                R.string.responsible_reminder,
                                state.breakThreshold
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = stringResource(R.string.responsible_seed_title),
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.responsible_seed_session,
                                session.sessionSeed
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = Gold
                        )
                        Text(
                            text = stringResource(
                                R.string.responsible_seed_spin,
                                session.lastSpinSeed
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = Gold
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryButton(
                        textRes = R.string.cta_take_break,
                        onClick = onTakeBreak,
                        modifier = Modifier.weight(1f)
                    )
                    SecondaryButton(
                        textRes = R.string.cta_menu,
                        onClick = onMenu,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCard(titleRes: Int, bodyRes: Int) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(bodyRes),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }
    }
}
