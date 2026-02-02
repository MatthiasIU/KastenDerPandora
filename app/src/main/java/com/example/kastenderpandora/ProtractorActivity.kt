package com.example.kastenderpandora

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.*

class ProtractorActivity : BaseToolActivity() {

    override val toolTitle = R.string.protractor
    override val layoutResId = R.layout.activity_protractor
    override val enableSwipeBack = true

    private lateinit var viewFinder: PreviewView
    private lateinit var protractorView: ProtractorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewFinder = findViewById(R.id.viewFinder)
        protractorView = findViewById(R.id.protractorView)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1001)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview)
            } catch (e: Exception) { e.printStackTrace() }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(rc: Int, p: Array<out String>, rg: IntArray) {
        super.onRequestPermissionsResult(rc, p, rg)
        if (rc == 1001 && allPermissionsGranted()) startCamera()
    }

    // --- Custom View für den Winkelmesser ---
    class ProtractorView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
        private var angle1 = 210.0 // Linker Arm (Schenkel)
        private var angle2 = 330.0 // Rechter Arm (Schenkel)
        private var activeSchenkel = 0

        private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#3498db") // Blau aus dem Beispiel
            strokeWidth = 12f
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }

        private val scalePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            strokeWidth = 4f
            style = Paint.Style.STROKE
        }

        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#2ecc71") // Grün für den Winkelwert
            textSize = 110f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
            setShadowLayer(15f, 0f, 0f, Color.BLACK) // Maximale Sichtbarkeit auf Kamera
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val cx = width / 2f
            val cy = height * 0.75f // Drehpunkt leicht nach oben für Landscape

            // Skala-Radius basierend auf der kleinsten Bildschirmseite (Landscape-Fix)
            val radius = min(width, height) * 0.35f
            // Armlänge: Deutlich länger als der Radius der Skala
            val armLength = radius * 1.5f

            // 1. Skala (Halbkreis)
            val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
            scalePaint.alpha = 130
            canvas.drawArc(rect, 180f, 180f, false, scalePaint)

            // Gradstriche
            for (i in 0..180 step 10) {
                val rad = Math.toRadians((i + 180).toDouble())
                val innerR = if (i % 30 == 0) 0.88f else 0.94f
                val startX = cx + (radius * innerR * cos(rad)).toFloat()
                val startY = cy + (radius * innerR * sin(rad)).toFloat()
                val endX = cx + (radius * cos(rad)).toFloat()
                val endY = cy + (radius * sin(rad)).toFloat()
                canvas.drawLine(startX, startY, endX, endY, scalePaint)
            }

            // 2. Die beiden Arme (Schenkel) zeichnen
            drawArm(canvas, cx, cy, armLength, angle1)
            drawArm(canvas, cx, cy, armLength, angle2)

            // 3. Zentrumspunkt
            linePaint.style = Paint.Style.FILL
            canvas.drawCircle(cx, cy, 18f, linePaint)

            // 4. Winkelwert oben anzeigen
            val diff = abs(angle1 - angle2)
            canvas.drawText(String.format("%.1f°", diff), cx, 150f, textPaint)
        }

        private fun drawArm(canvas: Canvas, cx: Float, cy: Float, length: Float, angle: Double) {
            val rad = Math.toRadians(angle)
            val x = cx + (length * cos(rad)).toFloat()
            val y = cy + (length * sin(rad)).toFloat()
            linePaint.style = Paint.Style.STROKE
            canvas.drawLine(cx, cy, x, y, linePaint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val cx = width / 2f
            val cy = height * 0.75f

            // Mathematisch korrekte Winkelberechnung zum Drehpunkt
            var touchAngle = Math.toDegrees(atan2((event.y - cy).toDouble(), (event.x - cx).toDouble()))
            if (touchAngle < 0) touchAngle += 360.0

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Prüfen, welcher Arm näher ist (Normalisierte Distanz)
                    val d1 = abs(touchAngle - angle1)
                    val d2 = abs(touchAngle - angle2)
                    activeSchenkel = if (d1 < d2) 1 else 2
                }
                MotionEvent.ACTION_MOVE -> {
                    // Nur im oberen Bereich (ca. 180° bis 360°) reagieren
                    if (touchAngle in 170.0..370.0) {
                        if (activeSchenkel == 1) angle1 = touchAngle else angle2 = touchAngle
                        invalidate()
                    }
                }
            }
            return true
        }
    }
}