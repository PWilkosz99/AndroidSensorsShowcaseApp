package com.agh.sensorsapp.ui.drawer

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
import com.agh.sensorsapp.ui.AccelerometerScreen
import com.agh.sensorsapp.ui.AmbientTemperatureScreen
import com.agh.sensorsapp.ui.HomeScreen
import com.agh.sensorsapp.ui.SettingsScreen
import com.agh.sensorsapp.ui.GravityScreen
import com.agh.sensorsapp.ui.GyroscopeScreen
import com.agh.sensorsapp.ui.LinearAccelerationScreen
import com.agh.sensorsapp.ui.MagneticScreen
import com.agh.sensorsapp.ui.ProximityScreen
import com.agh.sensorsapp.ui.RotationVectorScreen
import com.agh.sensorsapp.ui.StepCounterAndDetectorScreen
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
        NavigationItem(
            title = "Accelerometer",
            route = "accelerometer",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Gravity",
            route = "gravity",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Gyroscope",
            route = "gyroscope",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Linear Acceleration",
            route = "linear_acceleration",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Step Counter and Detector",
            route = "step_counter_and_detector",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Game and Geomagnetic Rotation Vector",
            route = "game_and_geomagnetic_rotation_vector",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Magnetic",
            route = "magnetic",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Proximity",
            route = "proximity",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
        ),
        NavigationItem(
            title = "Ambient Temperature",
            route = "ambient_temperature",
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
                    composable("accelerometer") {
                        AccelerometerScreen()
                    }
                    composable("gravity") {
                        GravityScreen()
                    }
                    composable("gyroscope") {
                        GyroscopeScreen()
                    }
                    composable("linear_acceleration") {
                        LinearAccelerationScreen()
                    }
                    composable("step_counter_and_detector") {
                        StepCounterAndDetectorScreen()
                    }
                    composable("game_and_geomagnetic_rotation_vector") {
                        RotationVectorScreen()
                    }
                    composable("magnetic") {
                        MagneticScreen()
                    }
                    composable("proximity") {
                        ProximityScreen()
                    }
                    composable("ambient_temperature") {
                        AmbientTemperatureScreen()
                    }
                }
            }
        }
    }
}