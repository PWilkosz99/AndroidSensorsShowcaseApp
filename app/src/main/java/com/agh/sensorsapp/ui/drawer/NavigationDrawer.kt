package com.agh.sensorsapp.ui.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Gesture
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MotionPhotosAuto
import androidx.compose.material.icons.outlined.MotionPhotosOn
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDamage
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.agh.sensorsapp.ui.GravityScreen
import com.agh.sensorsapp.ui.GyroscopeScreen
import com.agh.sensorsapp.ui.LightScreen
import com.agh.sensorsapp.ui.LinearAccelerationScreen
import com.agh.sensorsapp.ui.MagneticScreen
import com.agh.sensorsapp.ui.PressureScreen
import com.agh.sensorsapp.ui.ProximityScreen
import com.agh.sensorsapp.ui.RelativeHumidityScreen
import com.agh.sensorsapp.ui.RotationVectorScreen
import com.agh.sensorsapp.ui.SignificantMotionScreen
import com.agh.sensorsapp.ui.StepCounterAndDetectorScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Drawer(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Home", "home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationItem("Accelerometer", "accelerometer", Icons.Filled.MotionPhotosOn, Icons.Outlined.MotionPhotosOn),
        NavigationItem("Gravity", "gravity", Icons.Filled.Public, Icons.Outlined.Public),
        NavigationItem("Gyroscope", "gyroscope", Icons.Filled.Gesture, Icons.Outlined.Gesture),
        NavigationItem("Linear Acceleration", "linear_acceleration", Icons.Filled.Explore, Icons.Outlined.Explore),
        NavigationItem("Significant Motion", "significant_motion", Icons.Filled.MotionPhotosAuto, Icons.Outlined.MotionPhotosAuto),
        NavigationItem("Step Counter and Detector", "step_counter_and_detector", Icons.Filled.DirectionsRun, Icons.Outlined.DirectionsRun),
        NavigationItem("Game and Geomagnetic Rotation Vector", "game_and_geomagnetic_rotation_vector", Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports),
        NavigationItem("Magnetic", "magnetic", Icons.Filled.HelpOutline, Icons.Outlined.HelpOutline),
        NavigationItem("Proximity", "proximity", Icons.Filled.PhoneInTalk, Icons.Outlined.PhoneInTalk),
        NavigationItem("Ambient Temperature", "ambient_temperature", Icons.Filled.Thermostat, Icons.Outlined.Thermostat),
        NavigationItem("Light", "light", Icons.Filled.WbSunny, Icons.Outlined.WbSunny),
        NavigationItem("Pressure", "pressure", Icons.Filled.Speed, Icons.Outlined.Speed),
        NavigationItem("Relative Humidity", "humidity", Icons.Filled.WaterDamage, Icons.Outlined.WaterDamage),
    )

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        items.forEachIndexed { index, item ->
                            DrawerItem(
                                item = item,
                                isSelected = index == selectedItemIndex,
                                onItemClick = {
                                    selectedItemIndex = index
                                    scope.launch {
                                        drawerState.close()
                                        navController.navigate(item.route)
                                    }
                                }
                            )
                        }
                    }
                }
            },
            drawerState = drawerState
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
                    composable("significant_motion") {
                        SignificantMotionScreen()
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
                    composable("light") {
                        LightScreen()
                    }
                    composable("pressure") {
                        PressureScreen()
                    }
                    composable("humidity") {
                        RelativeHumidityScreen()
                    }
                }
            }
        }
    }
}