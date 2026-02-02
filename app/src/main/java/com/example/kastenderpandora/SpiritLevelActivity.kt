package com.example.kastenderpandora

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.roundToInt

class SpiritLevelActivity : BaseToolActivity(), SensorEventListener {

    override val toolTitle = R.string.spirit_level
    override val layoutResId = R.layout.activity_spirit_level
    override val enableSwipeBack = true

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var spiritLevelView: SpiritLevelView
    private lateinit var viewFinder: PreviewView

    // Smoothing (Low-pass filter) variables
    private val smoothingFactor = 0.15f
    private var filteredAngle = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewFinder = findViewById(R.id.viewFinder)
        spiritLevelView = findViewById(R.id.spiritLevelView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val rotation = windowManager.defaultDisplay.rotation
        val ax = event.values[0]
        val ay = event.values[1]

        // 1. Calculate raw angle based on device orientation
        val rawAngle = when (rotation) {
            Surface.ROTATION_90 -> Math.toDegrees(atan2(ay.toDouble(), ax.toDouble())).toFloat()
            Surface.ROTATION_270 -> Math.toDegrees(atan2(-ay.toDouble(), -ax.toDouble())).toFloat()
            Surface.ROTATION_180 -> Math.toDegrees(atan2(ax.toDouble(), -ay.toDouble())).toFloat()
            else -> Math.toDegrees(atan2(-ax.toDouble(), ay.toDouble())).toFloat()
        }

        // 2. Apply Low-Pass Filter (Smoothing)
        filteredAngle = filteredAngle + smoothingFactor * (rawAngle - filteredAngle)

        spiritLevelView.updateRotation(filteredAngle)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(rc: Int, p: Array<out String>, rg: IntArray) {
        super.onRequestPermissionsResult(rc, p, rg)
        if (rc == 1001 && allPermissionsGranted()) startCamera()
    }

    // --- Custom View for the Overlay ---
    class SpiritLevelView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
        private var currentAngle: Float = 0f
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 140f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
            setShadowLayer(12f, 0f, 0f, Color.BLACK) // Critical for readability over camera
        }

        fun updateRotation(angle: Float) {
            this.currentAngle = angle
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val w = width.toFloat()
            val h = height.toFloat()
            val isLevel = abs(currentAngle) < 1.0f

            // Liquid Color (Cyan for visibility, Green when level)
            paint.color = if (isLevel) Color.parseColor("#4CAF50") else Color.parseColor("#00BCD4")
            paint.style = Paint.Style.FILL

            canvas.save()
            canvas.rotate(-currentAngle, w / 2, h / 2)
            // Draw the liquid rectangle (covers bottom half)
            canvas.drawRect(-w, h / 2, w * 2, h * 2, paint)
            canvas.restore()

            // Reference UI Lines
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 6f
            canvas.drawCircle(w / 2, h / 2, 100f, paint)
            canvas.drawLine(w * 0.1f, h / 2, w * 0.4f, h / 2, paint)
            canvas.drawLine(w * 0.6f, h / 2, w * 0.9f, h / 2, paint)

            // Smoothed Text readout
            val displayAngle = abs(currentAngle.roundToInt())
            canvas.drawText("${displayAngle}°", w / 2, h / 2 + 250f, textPaint)
        }
    }
}