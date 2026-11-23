package com.Fidmowe7.Loskow.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val START_CREDITS = 500
    const val BET = 20
    const val REFILL_CREDITS = 250

    const val REELS = 3
    const val ROWS = 3
    const val CELLS = REELS * ROWS
    const val PAYLINES = 3

    const val METER_MAX = 5
    const val MAP_NODES = 8

    const val CASE_COUNT = 3
    const val TILES_PER_CASE = 5
    const val TILE_SHARD_COST = 4
    const val CASE_BONUS_STEP = 2

    const val VAULT_DOORS = 9
    const val VAULT_PICKS = 3
    const val VAULT_ATTEMPTS = 20

    const val REWARD_DAYS = 7

    const val NEAR_WIN_CAPTION_MS = 1200L
    const val WIN_ROLLUP_MS = 600L
    const val BONUS_HANDOFF_MS = 750L
    const val OUT_OF_CREDITS_MS = 900L
    const val PITY_AFTER_LOSSES = 5
    const val MAX_SHARDS_PER_WIN = 5

    val SPIN_MS = intArrayOf(2400, 1600, 1000)
    val STAGGER_MS = intArrayOf(320, 220, 140)
    val BREAK_OPTIONS = intArrayOf(50, 100, 200)

    const val DEFAULT_SPEED_INDEX = 1
    const val DEFAULT_BREAK_INDEX = 1
}
