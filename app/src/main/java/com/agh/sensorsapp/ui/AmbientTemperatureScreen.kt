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
fun AmbientTemperatureScreen() {
    val context = LocalContext.current

    val ambientTemperatureSensorListener = remember { AmbientTemperatureSensorListener(context) }

    DisposableEffect(ambientTemperatureSensorListener) {
        ambientTemperatureSensorListener.startListening()

        onDispose {
            ambientTemperatureSensorListener.stopListening()
        }
    }

    val ambientTemperatureState by ambientTemperatureSensorListener.ambientTemperature.collectAsState()

    val ambientTemperatureValue = ambientTemperatureState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (ambientTemperatureSensorListener.isAvailable) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Ambient Temperature Sensor Value", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Value: $ambientTemperatureValue")

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            Text(
                text = "Ambient Temperature sensor is not available on this device",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

class AmbientTemperatureSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val ambientTemperatureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    private val _ambientTemperature = MutableStateFlow(0f)
    val ambientTemperature: StateFlow<Float> = _ambientTemperature

    val isAvailable: Boolean
        get() = ambientTemperatureSensor != null

    fun startListening() {
        ambientTemperatureSensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    val value = it.values[0]
                    _ambientTemperature.value = value
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}