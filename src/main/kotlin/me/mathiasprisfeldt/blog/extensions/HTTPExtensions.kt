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

fun HttpServletResponse.json(httpCode: Int, msg: String = ""): JSONResponse {
    return JSONResponse(httpCode, msg)
}

fun <T> HttpServletResponse.dataJson(httpCode: Int, msg: String = "", data: T? = null): DataJSONResponse<T> {
    return DataJSONResponse(httpCode, msg, Optional.ofNullable(data))
}

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