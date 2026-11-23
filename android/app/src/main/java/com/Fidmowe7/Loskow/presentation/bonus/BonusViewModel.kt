package com.Fidmowe7.Loskow.presentation.bonus

import androidx.lifecycle.ViewModel
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.VaultDoor
import com.Fidmowe7.Loskow.domain.model.VaultPrize
import com.Fidmowe7.Loskow.domain.repository.CaseRepository
import com.Fidmowe7.Loskow.domain.usecase.BuildVaultGridUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BonusUiState(
    val doors: List<VaultDoor> = emptyList(),
    val picksLeft: Int = GameConfig.VAULT_PICKS,
    val credits: Int = 0,
    val shards: Int = 0,
    val clues: Int = 0,
    val finished: Boolean = false
)

class BonusViewModel(private val cases: CaseRepository) : ViewModel() {

    private val buildGrid = BuildVaultGridUseCase()

    private val backing = MutableStateFlow(freshRound())
    val uiState: StateFlow<BonusUiState> = backing.asStateFlow()

    fun pick(index: Int) {
        val current = backing.value
        if (current.finished || current.picksLeft <= 0) return
        val door = current.doors.getOrNull(index) ?: return
        if (door.revealed) return
        val doors = current.doors.map { if (it.index == index) it.copy(revealed = true) else it }
        if (door.prize == VaultPrize.CollectAll) {
            backing.value = current.copy(
                doors = doors.map { it.copy(revealed = true) },
                picksLeft = 0,
                credits = current.credits + sumCredits(current.doors),
                shards = current.shards + sumShards(current.doors),
                clues = current.clues + sumClues(current.doors),
                finished = true
            )
            return
        }
        val picksLeft = current.picksLeft - 1
        backing.value = current.copy(
            doors = doors,
            picksLeft = picksLeft,
            credits = current.credits + door.prize.credits,
            shards = current.shards + door.prize.shards,
            clues = current.clues + door.prize.clues,
            finished = picksLeft <= 0
        )
    }

    fun reset() {
        backing.update { freshRound() }
    }

    private fun sumCredits(doors: List<VaultDoor>): Int =
        doors.sumOf { it.prize.credits }

    private fun sumShards(doors: List<VaultDoor>): Int =
        doors.sumOf { it.prize.shards }

    private fun sumClues(doors: List<VaultDoor>): Int =
        doors.sumOf { it.prize.clues }

    private fun freshRound(): BonusUiState {
        val saved = cases.state.value
        val seed = (saved.vaultVisits + 1).toLong() * 7919L xor (saved.totalSpins + 1).toLong() * 104729L
        return BonusUiState(doors = buildGrid(seed))
    }
}
