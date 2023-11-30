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
fun MagneticScreen() {
    val context = LocalContext.current

    val magneticFieldListener = remember { MagneticFieldSensorListener(context) }

    DisposableEffect(magneticFieldListener) {
        magneticFieldListener.startListening()

        onDispose {
            magneticFieldListener.stopListening()
        }
    }

    val magneticFieldState by magneticFieldListener.magneticField.collectAsState()

    val (magX, magY, magZ) = magneticFieldState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Magnetic Field Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "X: $magX")
            Text(text = "Y: $magY")
            Text(text = "Z: $magZ")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

class MagneticFieldSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val magneticFieldSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val _magneticField = MutableStateFlow(Triple(0f, 0f, 0f))
    val magneticField: StateFlow<Triple<Float, Float, Float>> = _magneticField

    fun startListening() {
        magneticFieldSensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    val values = Triple(it.values[0], it.values[1], it.values[2])
                    _magneticField.value = values
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
