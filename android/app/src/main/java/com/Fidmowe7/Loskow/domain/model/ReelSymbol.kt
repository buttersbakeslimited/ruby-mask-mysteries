package com.Fidmowe7.Loskow.domain.model

enum class ReelSymbol(val weight: Int, val payout: Int, val spriteIndex: Int) {
    RubyMask(2, 18, 1),
    GoldenSeven(4, 9, 5),
    RubyGem(6, 4, 2),
    SapphireGem(8, 2, 3),
    BallroomBell(10, 1, 4);

    val isWild: Boolean
        get() = this == RubyMask

    val isHigh: Boolean
        get() = this == RubyMask || this == GoldenSeven || this == RubyGem
}
