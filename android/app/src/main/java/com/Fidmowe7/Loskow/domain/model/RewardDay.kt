package com.Fidmowe7.Loskow.domain.model

enum class RewardState { Claimed, Ready, Locked }

data class RewardDay(
    val day: Int,
    val credits: Int,
    val shards: Int,
    val state: RewardState
)

enum class RewardOutcome { Granted, AlreadyClaimed, ClockSkew }

data class RewardClaim(
    val outcome: RewardOutcome,
    val credits: Int,
    val shards: Int,
    val day: Int,
    val streakReset: Boolean
)
