package com.Fidmowe7.Loskow.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.core.theme.Gold
import com.Fidmowe7.Loskow.core.theme.Ruby
import com.Fidmowe7.Loskow.core.theme.TextMuted
import com.Fidmowe7.Loskow.core.theme.TextPrimary
import com.Fidmowe7.Loskow.core.theme.TextSecondary
import com.Fidmowe7.Loskow.core.util.formatAmount

data class BoardRow(
    val rank: Int,
    val nameRes: Int,
    val score: Int,
    val isPlayer: Boolean
)

@Composable
fun LeaderboardStrip(
    rows: List<BoardRow>,
    modifier: Modifier = Modifier,
    emptyMessageRes: Int = R.string.board_empty
) {
    GlassCard(modifier = modifier.fillMaxWidth(), corner = 16.dp) {
        if (rows.isEmpty()) {
            EmptyState(messageRes = emptyMessageRes, iconRes = R.drawable.ic_star)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (row in rows) {
                    BoardLine(row)
                }
            }
        }
    }
}

@Composable
private fun BoardLine(row: BoardRow) {
    val background = if (row.isPlayer) Ruby.copy(alpha = 0.14f) else Color.Transparent
    val border = if (row.isPlayer) Ruby.copy(alpha = 0.55f) else Color.Transparent
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .background(background, RoundedCornerShape(8.dp))
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = row.rank.toString(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = Gold,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(18.dp)
        )
        Text(
            text = stringResource(row.nameRes),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = if (row.isPlayer) TextPrimary else TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatAmount(row.score),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = if (row.isPlayer) Gold else TextMuted
        )
    }
}
