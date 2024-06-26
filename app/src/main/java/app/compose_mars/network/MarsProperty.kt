package app.compose_mars.network

import com.squareup.moshi.Json

data class MarsProperty(
    val id: String,
    val type: String,
    val price: Double,
    @Json(name = "img_src")
    val imgSrcUrl: String
){
    val isRental
        get() = type == "rent"
}
