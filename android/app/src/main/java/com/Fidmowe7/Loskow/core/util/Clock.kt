package com.Fidmowe7.Loskow.core.util

const val MILLIS_PER_DAY = 86400000L

fun todayEpochDay(): Long = System.currentTimeMillis() / MILLIS_PER_DAY

fun hoursUntilTomorrow(): Int {
    val remainder = System.currentTimeMillis() % MILLIS_PER_DAY
    val left = MILLIS_PER_DAY - remainder
    return ((left + 3599999L) / 3600000L).toInt()
}
