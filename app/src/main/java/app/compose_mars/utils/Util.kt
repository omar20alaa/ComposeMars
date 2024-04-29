package app.compose_mars.utils

fun redirectedImgSrcUrl(url: String): String {
    return url.replace(
        "http://mars.jpl.nasa.gov",
        "https://mars.nasa.gov"
    )
}