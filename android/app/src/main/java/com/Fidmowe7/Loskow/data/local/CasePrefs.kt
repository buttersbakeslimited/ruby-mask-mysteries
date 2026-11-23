package com.Fidmowe7.Loskow.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Fidmowe7.Loskow.core.config.GameConfig
import com.Fidmowe7.Loskow.domain.model.CaseState
import com.Fidmowe7.Loskow.domain.model.MissionId

class CasePrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ruby_mask_case", Context.MODE_PRIVATE)

    fun read(): CaseState {
        val started = prefs.getBoolean(KEY_FIRST_RUN, false)
        if (!started) {
            prefs.edit()
                .putBoolean(KEY_FIRST_RUN, true)
                .putInt(KEY_CREDITS, GameConfig.START_CREDITS)
                .apply()
        }
        return CaseState(
            credits = prefs.getInt(KEY_CREDITS, GameConfig.START_CREDITS),
            shards = prefs.getInt(KEY_SHARDS, 0),
            bestSessionTotal = prefs.getInt(KEY_BEST_SESSION, 0),
            bestWin = prefs.getInt(KEY_BEST_WIN, 0),
            totalSpins = prefs.getInt(KEY_TOTAL_SPINS, 0),
            unlockedTiles = readTiles(),
            caseBonusPercent = prefs.getInt(KEY_CASE_BONUS, 0),
            missionProgress = readMissionProgress(),
            missionClaimed = readMissionClaimed(),
            mapNode = prefs.getInt(KEY_MAP_NODE, 1),
            rewardDay = prefs.getInt(KEY_REWARD_DAY, 1),
            lastClaimEpochDay = prefs.getLong(KEY_LAST_CLAIM, 0L),
            vaultVisits = prefs.getInt(KEY_VAULT_VISITS, 0)
        )
    }

    fun write(state: CaseState) {
        val editor = prefs.edit()
        editor.putBoolean(KEY_FIRST_RUN, true)
        editor.putInt(KEY_CREDITS, state.credits)
        editor.putInt(KEY_SHARDS, state.shards)
        editor.putInt(KEY_BEST_SESSION, state.bestSessionTotal)
        editor.putInt(KEY_BEST_WIN, state.bestWin)
        editor.putInt(KEY_TOTAL_SPINS, state.totalSpins)
        editor.putInt(KEY_TILES, tilesToMask(state.unlockedTiles))
        editor.putInt(KEY_CASE_BONUS, state.caseBonusPercent)
        for (id in MissionId.entries) {
            editor.putInt(KEY_MISSION_PREFIX + id.name, state.missionProgress[id] ?: 0)
            editor.putBoolean(KEY_CLAIM_PREFIX + id.name, state.missionClaimed.contains(id))
        }
        editor.putInt(KEY_MAP_NODE, state.mapNode)
        editor.putInt(KEY_REWARD_DAY, state.rewardDay)
        editor.putLong(KEY_LAST_CLAIM, state.lastClaimEpochDay)
        editor.putInt(KEY_VAULT_VISITS, state.vaultVisits)
        editor.apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun readTiles(): Set<Int> {
        val mask = prefs.getInt(KEY_TILES, 0)
        val total = GameConfig.CASE_COUNT * GameConfig.TILES_PER_CASE
        return (0 until total).filter { ((mask shr it) and 1) == 1 }.toSet()
    }

    private fun tilesToMask(tiles: Set<Int>): Int {
        var mask = 0
        for (tile in tiles) {
            mask = mask or (1 shl tile)
        }
        return mask
    }

    private fun readMissionProgress(): Map<MissionId, Int> =
        MissionId.entries.associateWith { prefs.getInt(KEY_MISSION_PREFIX + it.name, 0) }

    private fun readMissionClaimed(): Set<MissionId> =
        MissionId.entries.filter { prefs.getBoolean(KEY_CLAIM_PREFIX + it.name, false) }.toSet()

    private companion object {
        const val KEY_FIRST_RUN = "first_run_done"
        const val KEY_CREDITS = "credits"
        const val KEY_SHARDS = "shards"
        const val KEY_BEST_SESSION = "best_session_total"
        const val KEY_BEST_WIN = "best_win"
        const val KEY_TOTAL_SPINS = "total_spins"
        const val KEY_TILES = "collection_unlocks"
        const val KEY_CASE_BONUS = "case_bonus_percent"
        const val KEY_MISSION_PREFIX = "mission_progress_"
        const val KEY_CLAIM_PREFIX = "mission_claimed_"
        const val KEY_MAP_NODE = "map_node"
        const val KEY_REWARD_DAY = "reward_day"
        const val KEY_LAST_CLAIM = "last_claim_epoch_day"
        const val KEY_VAULT_VISITS = "vault_visits"
    }
}
