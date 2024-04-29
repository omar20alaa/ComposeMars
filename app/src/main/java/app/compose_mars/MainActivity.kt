@file:OptIn(ExperimentalMaterial3Api::class)

package app.compose_mars

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.compose_mars.network.MarsApiFilter
import app.compose_mars.ui.theme.ComposeMarsTheme
import app.compose_mars.view.DetailsScreen
import app.compose_mars.view.HomeScreen
import app.compose_mars.viewmodel.MarsViewModel

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    private val marsViewModel: MarsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MarsCompose(marsViewModel)
        }
    }
}


@ExperimentalFoundationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MarsCompose(marsViewModel: MarsViewModel) {
    val navController = rememberNavController()
    var canPop by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var dropDownMenuExpanded by remember { mutableStateOf(false) }
    var appTitle by remember { mutableStateOf("") }

    navController.addOnDestinationChangedListener { controller, _, _ ->
        canPop = controller.previousBackStackEntry != null
    }

    val  navigationIcon: @Composable () -> Unit =
        if (canPop) {
            {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        } else {
            {}
        }

    Scaffold(
        topBar = {
                TopAppBar(
                    title = { Text(appTitle) },
                    navigationIcon = navigationIcon,
                    actions = {
                        if (showDropDownMenu) {
                            IconButton(onClick = {
                                dropDownMenuExpanded = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Dropdown menu"
                                )

                                DropdownMenu(
                                    expanded = dropDownMenuExpanded,
                                    onDismissRequest = { dropDownMenuExpanded = false }) {
                                    DropdownMenuItem(
                                        text = {
                                            Text("All Properties")
                                        },
                                        onClick = {
                                            marsViewModel.updateFilter(MarsApiFilter.SHOW_ALL)
                                            dropDownMenuExpanded = false
                                        })

                                    DropdownMenuItem(
                                        text = {
                                            Text("Rental Properties")
                                        },
                                        onClick = {
                                            marsViewModel.updateFilter(MarsApiFilter.SHOW_RENT)
                                            dropDownMenuExpanded = false
                                        })

                                    DropdownMenuItem(
                                        text = {
                                            Text("FOR SELL")
                                        },
                                        onClick = {
                                            marsViewModel.updateFilter(MarsApiFilter.SHOW_BUY)
                                            dropDownMenuExpanded = false
                                        })
                                }
                            }
                        }
                    }
                )
        },
        content = {
            NavHost(navController = navController, startDestination = "home") {
                composable("home"){
                    HomeScreen(
                        navController,
                        marsViewModel,
                        onSetTitle = { appTitle = it },
                        onShowDropdownMenu = {showDropDownMenu = it},
                    )
                }

                composable("details/{propertyId}") { backStackEntry ->
                    DetailsScreen(
                        backStackEntry.arguments?.getString("propertyId"),
                        marsViewModel,
                        onSetTitle = { appTitle = it },
                        onShowDropdownMenu = { showDropDownMenu = it }
                    )
                }

            }
        }
    )

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMarsTheme {
        Greeting("Android")
    }
}