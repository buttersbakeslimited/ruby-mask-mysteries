package com.Fidmowe7.Loskow.domain.model

enum class PreviewStatus { Unlocked, Locked, Ready, Claimed }

data class FeaturePreview(
    val index: Int,
    val spriteIndex: Int,
    val status: PreviewStatus
)
