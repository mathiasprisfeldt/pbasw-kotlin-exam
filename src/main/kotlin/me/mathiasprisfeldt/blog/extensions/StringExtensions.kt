package me.mathiasprisfeldt.blog.extensions

fun String.trimMultiLine(): String = this.trimIndent().replace(Regex("[\n\r]"), " ")

fun String.words() = Regex("""[\wæøå]+(-+[\wæøå]+)?""").findAll(this)
fun String.wordCount(): Int = this.words().count()
fun String.wordCountByLength(length: Int): Int = this.words().count { it.value.length >= length }

fun String.periodCount(): Int = this.count { it == '.' }

fun String.lix(): Pair<Int, String> {
    val wordCount = this.wordCount()
    if (wordCount == 0) return 0 to "Teksten har ingen ord."

    val periodCount = this.periodCount()
    if (periodCount == 0) return 0 to "Teksten har ingen punktummer."

    val longWordsCount = this.wordCountByLength(7)

    val lixAmount = Math.round((wordCount / periodCount.toFloat()) + ((longWordsCount * 100) / wordCount.toFloat()))

    val lixText = when(lixAmount) {
        in Int.MIN_VALUE..24 -> "Let tekst for alle læsere."
        in 25..34 -> "Let for øvede læsere."
        in 35..44 -> "Middel."
        in 45..54 -> "Svær."
        in 55..Int.MAX_VALUE -> "Meget svær."
        else -> "Ukendt."
    }

    return lixAmount to lixText
}