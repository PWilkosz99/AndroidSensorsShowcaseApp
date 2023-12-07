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
fun ProximityScreen() {
    val context = LocalContext.current

    val proximitySensorListener = remember { ProximitySensorListener(context) }

    DisposableEffect(proximitySensorListener) {
        proximitySensorListener.startListening()

        onDispose {
            proximitySensorListener.stopListening()
        }
    }

    val proximityState by proximitySensorListener.proximity.collectAsState()

    val proximityValue = proximityState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (proximitySensorListener.isAvailable) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Proximity Sensor Value",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Value: $proximityValue")

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            Text(
                text = "Proximity sensor is not available on this device",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

class ProximitySensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val proximitySensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    private val _proximity = MutableStateFlow(0f)
    val proximity: StateFlow<Float> = _proximity

    val isAvailable: Boolean
        get() = proximitySensor != null

    fun startListening() {
        proximitySensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_PROXIMITY) {
                    val value = it.values[0]
                    _proximity.value = value
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
