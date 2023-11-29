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
fun StepCounterAndDetectorScreen() {
    val context = LocalContext.current

    val stepCounterListener = remember { StepCounterSensorListener(context) }
    val stepDetectorListener = remember { StepDetectorSensorListener(context) }

    DisposableEffect(stepCounterListener, stepDetectorListener) {
        stepCounterListener.startListening()
        stepDetectorListener.startListening()

        onDispose {
            stepCounterListener.stopListening()
            stepDetectorListener.stopListening()
        }
    }

    val stepCounterState by stepCounterListener.stepCounter.collectAsState()
    val stepDetectorCountState by stepDetectorListener.stepDetectorCount.collectAsState()

    val steps = stepCounterState
    val detectedSteps = stepDetectorCountState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Step Counter and Detector Sensor Values", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Cumulative Steps: $steps")
            Text(text = "Detected Steps: $detectedSteps")

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

class StepDetectorSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepDetectorSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private val _stepDetectorCount = MutableStateFlow(0)
    val stepDetectorCount: StateFlow<Int> = _stepDetectorCount

    fun startListening() {
        stepDetectorSensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                    // Each step is detected, so increment the count
                    _stepDetectorCount.value += 1
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}

class StepCounterSensorListener(context: Context) {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val stepCounterSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _stepCounter = MutableStateFlow(0)
    val stepCounter: StateFlow<Int> = _stepCounter

    fun startListening() {
        stepCounterSensor?.let { sensor ->
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
                if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    val steps = it.values[0].toInt()
                    _stepCounter.value = steps
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }
    }
}
