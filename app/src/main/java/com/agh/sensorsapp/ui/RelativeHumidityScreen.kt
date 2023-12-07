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
fun RelativeHumidityScreen() {
    val context = LocalContext.current

    val humiditySensorListener = remember { RelativeHumiditySensorListener(context) }

    DisposableEffect(humiditySensorListener) {
        humiditySensorListener.startListening()

        onDispose {
            humiditySensorListener.stopListening()
        }
    }

    val humidityState by humiditySensorListener.relativeHumidity.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (humiditySensorListener.isAvailable) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Relative Humidity Sensor Value",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Value: $humidityState")

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            Text(
                text = "Relative humidity sensor is not available on this device",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

class RelativeHumiditySensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val humiditySensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

    private val _relativeHumidity = MutableStateFlow(0f)
    val relativeHumidity: StateFlow<Float> = _relativeHumidity

    val isAvailable: Boolean
        get() = humiditySensor != null

    fun startListening() {
        humiditySensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    val value = it.values[0]
                    _relativeHumidity.value = value
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
