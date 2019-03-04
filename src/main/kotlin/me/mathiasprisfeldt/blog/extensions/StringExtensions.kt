package me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.extensions

fun String.trimMultiLine(): String = this.trimIndent().replace(Regex("[\n\r]"), " ")

fun String.wordCount(): Int {
    val trimmed = this.replace(Regex("[.,]"), "").trim()

    if (trimmed.isEmpty()) return 0

    return trimmed.split(' ').count()
}

fun String.periodCount(): Int = this.count { it == '.' }
fun String.wordLengthCount(length: Int): Int = this.split(' ').count { it.length >= length }

fun String.lix(): Pair<Int, String> {
    val wordCount = this.wordCount()
    if (wordCount == 0) return Pair(0, "Teksten har ingen ord.")

    val periodCount = this.periodCount()
    if (periodCount == 0) return Pair(0, "Teksten har ingen punktummer.")

    val longWordsCount = this.wordLengthCount(7)

    val lixAmount = Math.round((wordCount / periodCount.toFloat()) + ((longWordsCount * 100) / wordCount.toFloat()))

    val lixText = when(lixAmount) {
        in Int.MIN_VALUE..24 -> "Let tekst for alle læsere."
        in 25..34 -> "Let for øvede læsere."
        in 35..44 -> "Middel."
        in 45..54 -> "Svær."
        in 55..Int.MAX_VALUE -> "Meget svær."
        else -> "Ukendt."
    }

    return Pair(lixAmount, lixText)
}