package com.Fidmowe7.Loskow.core.ui

import com.Fidmowe7.Loskow.R
import com.Fidmowe7.Loskow.domain.model.ReelSymbol

fun spriteRes(index: Int): Int = when (index) {
    1 -> R.drawable.sprite_1
    2 -> R.drawable.sprite_2
    3 -> R.drawable.sprite_3
    4 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}

fun symbolNameRes(symbol: ReelSymbol): Int = when (symbol) {
    ReelSymbol.RubyMask -> R.string.symbol_mask
    ReelSymbol.GoldenSeven -> R.string.symbol_seven
    ReelSymbol.RubyGem -> R.string.symbol_ruby
    ReelSymbol.SapphireGem -> R.string.symbol_sapphire
    ReelSymbol.BallroomBell -> R.string.symbol_bell
}
