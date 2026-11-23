package com.Fidmowe7.Loskow.core.util

fun formatAmount(value: Int): String {
    val negative = value < 0
    val digits = if (negative) (-value).toString() else value.toString()
    if (digits.length <= 3) return if (negative) "-" + digits else digits
    val builder = StringBuilder()
    var count = 0
    for (index in digits.lastIndex downTo 0) {
        builder.append(digits[index])
        count += 1
        if (count % 3 == 0 && index > 0) {
            builder.append(',')
        }
    }
    val grouped = builder.reverse().toString()
    return if (negative) "-" + grouped else grouped
}
