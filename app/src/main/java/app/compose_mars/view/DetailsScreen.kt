package app.compose_mars.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.compose_mars.utils.redirectedImgSrcUrl
import app.compose_mars.viewmodel.MarsViewModel
import coil.compose.rememberImagePainter
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DetailsScreen(
    propertyIndex: String?,
    marsViewModel: MarsViewModel,
    onSetTitle: (String) -> Unit,
    onShowDropdownMenu: (Boolean) -> Unit
) {
    val property = marsViewModel.getProperty(propertyIndex?.toInt() ?: 0)

    LaunchedEffect(Unit) {
        onSetTitle("Property # ${property?.id}")
        onShowDropdownMenu(false)
    }

    val format = NumberFormat.getCurrencyInstance(Locale.US)
    val formattedPrice = format.format(property?.price)

    val marsColor = Color(android.graphics.Color.parseColor("#9a7d55"))

    val propertyType = property?.type
        ?.replace("buy", "Sell")
        ?.replace("rent", "Rent")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Image(
            painter = rememberImagePainter(data = property?.let { redirectedImgSrcUrl(it.imgSrcUrl) }),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )
        Text("Property id: ${property?.id}")
        Text(
            formattedPrice,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = marsColor
        )
        Text(
            "For $propertyType",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 8.dp)
                .background(marsColor)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            color = Color.White
        )
    }
}