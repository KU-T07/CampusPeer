package com.example.campuspeer.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campuspeer.model.Routes
import com.example.campuspeer.navigation.BottomNavigationBar
import com.example.campuspeer.navigation.NavGraph
import kotlinx.coroutines.launch

@Composable
fun rememberViewModelStoreOwner(): ViewModelStoreOwner {
    val context = LocalContext.current
    return remember(context) { context as ViewModelStoreOwner }
}

val LocalNavGraphViewModelStoreOwner =
    staticCompositionLocalOf<ViewModelStoreOwner> {
        error("Undefined")
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navStoreOwner = rememberViewModelStoreOwner()
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(backStackEntry) {
        derivedStateOf {
            backStackEntry?.destination?.route?.let {
                Routes.getRoutes(it)
            } ?: run {
                Routes.Home
            }
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent()
            }
        }
    ) {

        CompositionLocalProvider(
            LocalNavGraphViewModelStoreOwner provides navStoreOwner
        ) {
            Scaffold(
                topBar = {
                    if (currentRoute.isRoot)
                        TopAppBar(
                            title = { Text(text = currentRoute.route) },
                            navigationIcon = {
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = ""
                                    )
                                }
                            }

                        )
                    else
                        TopAppBar(
                            title = { },
                            navigationIcon = {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = ""
                                    )
                                }
                            }
                        )
                },
                bottomBar = {
                    if (currentRoute.isRoot)
                        BottomNavigationBar(navController)
                },
                floatingActionButton = {
                    if (currentRoute == Routes.PostItemListScreen)
                        FloatingActionButton(onClick = {
                            navController.navigate(Routes.AddItem.route)
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "")
                        }
                }
            ) { contentPadding ->
                Column(modifier = Modifier.padding(contentPadding)) {
                    NavGraph(
                        navController = navController,
                        currentUserId = "dummyUserId"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}