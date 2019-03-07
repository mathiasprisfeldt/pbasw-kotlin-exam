package me.mathiasprisfeldt.blog.extensions

fun String.trimMultiLine(): String = this.trimIndent().replace(Regex("[\n\r]"), " ")

fun String.words() = Regex("""[\wæøå]+(-+[\wæøå]+)?""").findAll(this)
fun String.wordCount(): Int = this.words().count()
fun String.wordCountByLength(length: Int): Int = this.words().count { it.value.length >= length }

fun String.periodCount(): Int = this.count { it == '.' }

fun String.lix(): Pair<Int, String> {
    // If the text doesn't contain a single word, there's no Lix number.
    val wordCount = this.wordCount()
    if (wordCount == 0) return 0 to "Teksten har ingen ord."

    // If the text doesn't have and periods, we cant calculate the correct Lix number.
    val periodCount = this.periodCount()
    if (periodCount == 0) return 0 to "Teksten har ingen punktummer."

    val longWordsCount = this.wordCountByLength(7)

    val lixAmount = Math.round((wordCount / periodCount.toFloat()) + ((longWordsCount * 100) / wordCount.toFloat()))

    return lixAmount to lixAmount.toLix()
}