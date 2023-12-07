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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RawRotationVectorScreen() {
    val context = LocalContext.current

    val rawRotationVectorListener = remember { RawRotationVectorSensorListener(context) }

    DisposableEffect(rawRotationVectorListener) {
        rawRotationVectorListener.startListening()

        onDispose {
            rawRotationVectorListener.stopListening()
        }
    }

    val rawRotationVectorState by rawRotationVectorListener.rawRotationVector.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Raw Rotation Vector Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            RawRotationVectorValues(rawRotationVectorState)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RawRotationVectorValues(rawRotationVectorState: Triple<Float, Float, Float>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "X: ${rawRotationVectorState.first}")
        Text(text = "Y: ${rawRotationVectorState.second}")
        Text(text = "Z: ${rawRotationVectorState.third}")
    }
}

class RawRotationVectorSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val rawRotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val _rawRotationVector = MutableStateFlow(Triple(0f, 0f, 0f))
    val rawRotationVector: StateFlow<Triple<Float, Float, Float>> = _rawRotationVector

    fun startListening() {
        rawRotationVectorSensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val values = Triple(it.values[0], it.values[1], it.values[2])
                    _rawRotationVector.value = values
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
