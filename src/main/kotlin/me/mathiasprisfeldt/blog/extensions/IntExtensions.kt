package me.mathiasprisfeldt.blog.extensions

/**
 * From a given int it calculates what the lix in text would be.
 */
fun Int.toLixDesc() = when(this) {
    in Int.MIN_VALUE..24 -> "Let tekst for alle læsere."
    in 25..34 -> "Let for øvede læsere."
    in 35..44 -> "Middel."
    in 45..54 -> "Svær."
    in 55..Int.MAX_VALUE -> "Meget svær."
    else -> "Ukendt."
}