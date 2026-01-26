package com.example.kastenderpandora

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class LightActivity : BaseToolActivity() {

    override val toolTitle = R.string.light // Ensure this exists in strings.xml
    override val layoutResId = R.layout.activity_light

    private var isFlashlightOn = false
    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btnToggle = findViewById<Button>(R.id.btnToggle)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            // Usually, the first camera (0) is the back camera with the flash
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        fun updateUI() {
            if (isFlashlightOn) {
                btnToggle.text = "Turn OFF"
                // Change to a "Warning" or "High Emphasis" color
                btnToggle.setBackgroundColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_primary40))
                btnToggle.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                btnToggle.text = "Turn ON"
                // Reset to a standard Tonal/Outlined look
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
                // Handle cases where flash is unavailable or in use
                e.printStackTrace()
            }
        }

        updateUI()
    }

    // Safety: Turn off flash if the user leaves the activity
    override fun onPause() {
        super.onPause()
        if (isFlashlightOn) {
            try {
                cameraId?.let { cameraManager.setTorchMode(it, false) }
            } catch (e: Exception) { }
        }
    }
}