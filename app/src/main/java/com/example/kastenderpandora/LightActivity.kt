package com.example.kastenderpandora

import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.kastenderpandora.i18n.StringRes

class LightActivity : BaseToolActivity() {

    override val toolTitle = R.string.light
    override val layoutResId = R.layout.activity_light

    private var isFlashlightOn = false
    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnToggle = findViewById<Button>(R.id.btnToggle)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        fun updateUI() {
            if (isFlashlightOn) {
                btnToggle.setText(StringRes.Light.turnOff)
                btnToggle.setBackgroundColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_primary40))
                btnToggle.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                btnToggle.setText(StringRes.Light.turnOn)
                btnToggle.setBackgroundColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_neutral90))
                btnToggle.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            }
        }

        btnToggle.setOnClickListener {
            try {
                cameraId?.let { id ->
                    isFlashlightOn = !isFlashlightOn
                    cameraManager.setTorchMode(id, isFlashlightOn)
                    updateUI()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        updateUI()
    }

    override fun onPause() {
        super.onPause()
        if (isFlashlightOn) {
            try {
                cameraId?.let { cameraManager.setTorchMode(it, false) }
            } catch (_: Exception) { }
        }
    }
}