package com.example.kastenderpandora

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.widget.ImageView
import kotlin.math.roundToInt
import com.example.kastenderpandora.ui.ClickableTextView

class CompassActivity : BaseToolActivity(), SensorEventListener {

    override val toolTitle = R.string.compass
    override val layoutResId = R.layout.activity_compass
    override val enableSwipeBack = true

    private lateinit var sensorManager: SensorManager

    private var rotationVector: Sensor? = null
    private var accel: Sensor? = null
    private var magnet: Sensor? = null

    private val rotationMatrix = FloatArray(9)
    private val remappedMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    // Fallback (Accel+Mag)
    private val accelReading = FloatArray(3)
    private val magReading = FloatArray(3)
    private var hasAccel = false
    private var hasMag = false

    private lateinit var tvHeading: ClickableTextView
    private lateinit var ivNeedle: ImageView

    private var smoothedHeading = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvHeading = findViewById(R.id.tvHeading)
        ivNeedle = findViewById(R.id.ivNeedle)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.unregisterListener(this)

        when {
            rotationVector != null -> {
                sensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_UI)
            }
            accel != null && magnet != null -> {
                hasAccel = false
                hasMag = false
                sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI)
                sensorManager.registerListener(this, magnet, SensorManager.SENSOR_DELAY_UI)
            }
            else -> {
                tvHeading.text = "—"
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ROTATION_VECTOR -> {
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                computeAndShow(rotationMatrix)
            }

            Sensor.TYPE_ACCELEROMETER -> {
                lowPass(event.values, accelReading, alpha = 0.85f)
                hasAccel = true
                if (hasMag) {
                    val ok = SensorManager.getRotationMatrix(rotationMatrix, null, accelReading, magReading)
                    if (ok) computeAndShow(rotationMatrix)
                }
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                lowPass(event.values, magReading, alpha = 0.85f)
                hasMag = true
                if (hasAccel) {
                    val ok = SensorManager.getRotationMatrix(rotationMatrix, null, accelReading, magReading)
                    if (ok) computeAndShow(rotationMatrix)
                }
            }
        }
    }

    private fun computeAndShow(r: FloatArray) {
        // Display-Rotation berücksichtigen (sonst “komische” Azimuth-Werte je nach Drehung)
        val (axisX, axisY) = when (getDisplayRotation()) {
            Surface.ROTATION_0 -> SensorManager.AXIS_X to SensorManager.AXIS_Y
            Surface.ROTATION_90 -> SensorManager.AXIS_Y to SensorManager.AXIS_MINUS_X
            Surface.ROTATION_180 -> SensorManager.AXIS_MINUS_X to SensorManager.AXIS_MINUS_Y
            Surface.ROTATION_270 -> SensorManager.AXIS_MINUS_Y to SensorManager.AXIS_X
            else -> SensorManager.AXIS_X to SensorManager.AXIS_Y
        }

        SensorManager.remapCoordinateSystem(r, axisX, axisY, remappedMatrix)
        SensorManager.getOrientation(remappedMatrix, orientation)

        // orientation[0] = azimuth (rad)
        val rawDeg = normalizeDeg(Math.toDegrees(orientation[0].toDouble()).toFloat())

        // kleine Glättung gegen Zittern
        smoothedHeading = smoothAngle(smoothedHeading, rawDeg, factor = 0.15f)

        val shown = smoothedHeading.roundToInt()
        tvHeading.text = "$shown° ${cardinal(smoothedHeading)}"

        // Nadel zeigt nach Norden: View-Rotation entgegengesetzt zum Heading
        ivNeedle.rotation = -smoothedHeading
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun normalizeDeg(deg: Float): Float {
        var d = deg % 360f
        if (d < 0f) d += 360f
        return d
    }

    private fun cardinal(deg: Float): String {
        val dirs = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val idx = (((deg + 22.5f) / 45f).toInt()) % 8
        return dirs[idx]
    }

    private fun smoothAngle(current: Float, target: Float, factor: Float): Float {
        // kürzesten Weg über 0/360 nehmen
        var diff = (target - current + 540f) % 360f - 180f
        return normalizeDeg(current + diff * factor)
    }

    private fun lowPass(input: FloatArray, output: FloatArray, alpha: Float) {
        for (i in 0..2) output[i] = alpha * output[i] + (1f - alpha) * input[i]
    }

    @Suppress("DEPRECATION")
    private fun getDisplayRotation(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.rotation ?: Surface.ROTATION_0
        } else {
            windowManager.defaultDisplay.rotation
        }
    }
}
