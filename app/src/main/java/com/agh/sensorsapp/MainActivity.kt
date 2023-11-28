package com.agh.sensorsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.agh.sensorsapp.ui.theme.AndroidSensorsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSensorsAppTheme {
                val navController = rememberNavController()
                Drawer(navController = navController)
            }
        }
    }
}