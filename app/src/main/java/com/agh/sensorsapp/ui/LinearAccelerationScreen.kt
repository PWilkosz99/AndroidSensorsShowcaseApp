package com.agh.sensorsapp.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LinearAccelerationScreen() {
    val context = LocalContext.current

    val sensorListener = remember { LinearAccelerationSensorListener(context) }

    DisposableEffect(sensorListener) {
        sensorListener.startListening()

        onDispose {
            sensorListener.stopListening()
        }
    }

    val linearAccelerationState by sensorListener.linearAcceleration.collectAsState()

    val (x, y, z) = linearAccelerationState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Linear Acceleration Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "X: $x")
            Text(text = "Y: $y")
            Text(text = "Z: $z")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

class LinearAccelerationSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val linearAccelerationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    private val _linearAcceleration = MutableStateFlow(Triple(0f, 0f, 0f))
    val linearAcceleration: StateFlow<Triple<Float, Float, Float>> = _linearAcceleration

    fun startListening() {
        linearAccelerationSensor?.let { sensor ->
            sensorManager.registerListener(
                sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                    val values = Triple(it.values[0], it.values[1], it.values[2])
                    _linearAcceleration.value = values
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
