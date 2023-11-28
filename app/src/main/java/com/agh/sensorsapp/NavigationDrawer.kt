package com.agh.sensorsapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Drawer(navController: NavHostController) {
    val items = listOf(
        NavigationItem(
            title = "Home",
            route = "home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        NavigationItem(
            title = "Settings",
            route = "settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
    )

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        DrawerItem(item = item,
                            isSelected = index == selectedItemIndex,
                            onItemClick = {
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                    navController.navigate(item.route)
                                }
                            })
                    }
                }
            }, drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopBar(onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    })
                }
            ) {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen()
                    }
                    composable("settings") {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}