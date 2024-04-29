package app.compose_mars.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.compose_mars.R
import app.compose_mars.network.MarsProperty
import app.compose_mars.utils.redirectedImgSrcUrl
import app.compose_mars.viewmodel.MarsApiStatus
import app.compose_mars.viewmodel.MarsViewModel
import coil.compose.rememberImagePainter

@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    navController: NavController,
    marsViewModel: MarsViewModel,
    onSetTitle: (String) -> Unit,
    onShowDropdownMenu: (Boolean) -> Unit
) {
    val appTitle = stringResource(id = R.string.app_name)
    val status by marsViewModel.status.observeAsState(MarsApiStatus.ERROR)
    val properties by marsViewModel.properties.observeAsState(listOf())

    LaunchedEffect(Unit) {
        onSetTitle(appTitle)
        onShowDropdownMenu(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (status) {
            MarsApiStatus.DONE -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive( minSize = 160.dp)
                ) {
                    itemsIndexed(properties) { index, property ->
                        MarsPhotoGridLayout(
                            property,
                            onPhotoClicked = { navController.navigate("details/$index") }
                        )
                    }
                }
            }

            MarsApiStatus.LOADING -> {
                Image(
                    painter = rememberImagePainter(data =R.drawable.loading_animation),
                    contentDescription = "Loading Properties",
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Image(
                    painter = rememberImagePainter(data = R.drawable.ic_connection_error),
                    contentDescription = "Connection Error",
                    modifier = Modifier.fillMaxSize(0.5f)
                )

                Text(
                    "Error! Can't connect to the internet.\n" +
                            "Check your internet connection.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun MarsPhotoGridLayout(
    property: MarsProperty,
    onPhotoClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onPhotoClicked() },
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = rememberImagePainter(
                data = redirectedImgSrcUrl(property.imgSrcUrl),
                builder = {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image)
                }
            ),
            contentDescription = null,
            modifier = Modifier.height(150.dp),
            contentScale = ContentScale.Crop
        )

        if (!property.isRental) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Available for sell",
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .size(16.dp)
            )
        }
    }
}