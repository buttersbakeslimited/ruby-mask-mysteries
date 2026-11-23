package com.Fidmowe7.Loskow.domain.usecase

import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.VaultDoor
import com.Fidmowe7.Loskow.domain.model.VaultPrize
import kotlin.random.Random

class BuildVaultGridUseCase {

    private val fallback = listOf(
        VaultPrize.CreditsSmall,
        VaultPrize.ShardsSmall,
        VaultPrize.CaseClue,
        VaultPrize.CreditsLarge,
        VaultPrize.ShardsLarge,
        VaultPrize.CreditsSmall,
        VaultPrize.CollectAll,
        VaultPrize.CaseClue,
        VaultPrize.CreditsSmall
    )

    operator fun invoke(seed: Long): List<VaultDoor> {
        val random = Random(seed)
        val pool = VaultPrize.entries
        repeat(GameConfig.VAULT_ATTEMPTS) {
            val draw = List(GameConfig.VAULT_DOORS) { pool[random.nextInt(pool.size)] }
            if (isValid(draw)) return draw.toDoors()
        }
        return fallback.toDoors()
    }

    private fun isValid(prizes: List<VaultPrize>): Boolean {
        val shard = prizes.any { it.shards > 0 }
        val credit = prizes.any { it.credits > 0 }
        val collectAll = prizes.count { it == VaultPrize.CollectAll }
        return shard && credit && collectAll <= 1
    }

    private fun List<VaultPrize>.toDoors(): List<VaultDoor> =
        mapIndexed { index, prize -> VaultDoor(index, prize, false) }
}
