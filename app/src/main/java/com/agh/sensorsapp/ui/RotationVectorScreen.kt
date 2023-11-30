package com.agh.sensorsapp.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RotationVectorScreen() {
    val context = LocalContext.current

    val gameRotationVectorListener = remember { RotationVectorSensorListener(context, Sensor.TYPE_GAME_ROTATION_VECTOR) }
    val geomagneticRotationVectorListener = remember { RotationVectorSensorListener(context, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR) }

    DisposableEffect(gameRotationVectorListener, geomagneticRotationVectorListener) {
        gameRotationVectorListener.startListening()
        geomagneticRotationVectorListener.startListening()

        onDispose {
            gameRotationVectorListener.stopListening()
            geomagneticRotationVectorListener.stopListening()
        }
    }

    val gameRotationVectorState by gameRotationVectorListener.rotationVector.collectAsState()
    val geomagneticRotationVectorState by geomagneticRotationVectorListener.rotationVector.collectAsState()

    val (gameX, gameY, gameZ) = gameRotationVectorState
    val (geoX, geoY, geoZ) = geomagneticRotationVectorState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Game Rotation Vector Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "X: $gameX")
            Text(text = "Y: $gameY")
            Text(text = "Z: $gameZ")

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Geomagnetic Rotation Vector Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "X: $geoX")
            Text(text = "Y: $geoY")
            Text(text = "Z: $geoZ")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

class RotationVectorSensorListener(context: Context, private val sensorType: Int) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val rotationVectorSensor: Sensor? = sensorManager.getDefaultSensor(sensorType)

    private val _rotationVector = MutableStateFlow(Triple(0f, 0f, 0f))
    val rotationVector: StateFlow<Triple<Float, Float, Float>> = _rotationVector

    fun startListening() {
        rotationVectorSensor?.let { sensor ->
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
                if (it.sensor.type == sensorType) {
                    val values = Triple(it.values[0], it.values[1], it.values[2])
                    _rotationVector.value = values
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
