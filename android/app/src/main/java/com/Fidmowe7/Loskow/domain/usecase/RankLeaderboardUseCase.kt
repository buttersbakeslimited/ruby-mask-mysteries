package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.domain.model.LeaderboardEntry

class RankLeaderboardUseCase {

    operator fun invoke(rivalScores: List<Int>, playerBest: Int): List<LeaderboardEntry> {
        val rivals = rivalScores.mapIndexed { index, score ->
            LeaderboardEntry(index, score, false, 0)
        }
        val board = (rivals + LeaderboardEntry(-1, playerBest, true, 0))
            .sortedByDescending { it.score }
        return board.mapIndexed { index, entry -> entry.copy(rank = index + 1) }
    }

    fun playerRank(board: List<LeaderboardEntry>): Int =
        board.firstOrNull { it.isPlayer }?.rank ?: board.size
}
