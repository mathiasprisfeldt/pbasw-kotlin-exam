package me.mathiasprisfeldt.blog.extensions

import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

fun String.toSlug() = toLowerCase()
        .replace("\n", " ")
        .replace("[^a-z\\d\\s]".toRegex(), " ")
        .split(" ")
        .joinToString("-")
        .replace("-+".toRegex(), "-")

/**
 * Creates new JSON Response with no data attached.
 */
fun HttpServletResponse.json(httpCode: Int, msg: String = ""): JSONResponse {
    return JSONResponse(httpCode, msg)
}

/**
 * Creates a new JSON Response with optional data attached.
 */
fun <T> HttpServletResponse.dataJson(httpCode: Int, msg: String = "", data: T? = null): DataJSONResponse<T> {
    return DataJSONResponse(httpCode, msg, Optional.ofNullable(data))
}

/**
 * Creates new cookie with the correct max-age and path.
 */
fun String.authCookie(): Cookie {
    val cookie = Cookie("auth-token", this)
    cookie.path = "/"
    cookie.maxAge = 10000

    return cookie
}

data class DataJSONResponse<T> (
        val status: Int,
        val message: String,
        val data: Optional<T>
)

data class JSONResponse (
        val status: Int,
        val message: String
)