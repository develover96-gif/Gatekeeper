package com.example.ui.viewmodel

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StepGateViewModel(
  private val context: Context,
  val targetSteps: Int = 20
) : ViewModel(), SensorEventListener {

  private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

  private var startCount: Float? = null

  private val _stepsSoFar = MutableStateFlow(0)
  val stepsSoFar: StateFlow<Int> = _stepsSoFar.asStateFlow()

  private val _hasHardwareSensor = MutableStateFlow(true)
  val hasHardwareSensor: StateFlow<Boolean> = _hasHardwareSensor.asStateFlow()

  private val _isCleared = MutableStateFlow(false)
  val isCleared: StateFlow<Boolean> = _isCleared.asStateFlow()

  init {
    startListening()
  }

  fun startListening() {
    if (sensorManager == null) {
      _hasHardwareSensor.value = false
      return
    }

    val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    val stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    if (stepCounter == null && stepDetector == null) {
      _hasHardwareSensor.value = false
      return
    }

    _hasHardwareSensor.value = true

    if (stepCounter != null) {
      sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_UI)
    } else if (stepDetector != null) {
      sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_UI)
    }
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event == null) return

    if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
      val current = event.values[0]
      if (startCount == null) {
        startCount = current // baseline against first cumulative reading since reboot
      }
      val delta = (current - (startCount ?: current)).toInt()
      val count = delta.coerceAtLeast(0)
      _stepsSoFar.value = count
      if (count >= targetSteps) {
        _isCleared.value = true
        stopListening()
      }
    } else if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
      // Step detector triggers 1.0 each time a step is taken
      val current = _stepsSoFar.value + 1
      _stepsSoFar.value = current
      if (current >= targetSteps) {
        _isCleared.value = true
        stopListening()
      }
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // No-op
  }

  /**
   * Helper for testing/emulators or indoor simulation when physical walking is restricted.
   */
  fun recordManualStep() {
    val current = _stepsSoFar.value + 1
    _stepsSoFar.value = current
    if (current >= targetSteps) {
      _isCleared.value = true
      stopListening()
    }
  }

  fun stopListening() {
    sensorManager?.unregisterListener(this)
  }

  override fun onCleared() {
    super.onCleared()
    stopListening()
  }
}
