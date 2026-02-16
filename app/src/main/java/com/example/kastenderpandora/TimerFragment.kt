package com.example.kastenderpandora

import android.app.AlertDialog
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class TimerFragment : Fragment() {

    private lateinit var tvTimerTime: TextView
    private lateinit var btnStartTimer: Button
    private lateinit var btnResetTimer: Button

    private var isRunning = false
    private var remainingTimeMillis = 0L
    private var initialTimeMillis = 0L
    private var lastTickTime = 0L

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isRunning && remainingTimeMillis > 0) {
                val currentTime = System.currentTimeMillis()
                val elapsed = currentTime - lastTickTime
                lastTickTime = currentTime
                remainingTimeMillis -= elapsed

                if (remainingTimeMillis <= 0) {
                    remainingTimeMillis = 0
                    isRunning = false
                    btnStartTimer.text = getString(R.string.start)
                    updateTimeDisplay(0)
                    onTimerFinished()
                } else {
                    updateTimeDisplay(remainingTimeMillis)
                    handler.postDelayed(this, 100)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTimerTime = view.findViewById(R.id.tvTimerTime)
        btnStartTimer = view.findViewById(R.id.btnStartTimer)
        btnResetTimer = view.findViewById(R.id.btnResetTimer)

        // Tap on time to set timer
        tvTimerTime.setOnClickListener {
            if (!isRunning) {
                showTimePickerDialog()
            }
        }

        btnStartTimer.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        btnResetTimer.setOnClickListener {
            resetTimer()
        }

        // Initialize display
        updateTimeDisplay(remainingTimeMillis)
    }

    private fun showTimePickerDialog(startAfterSet: Boolean = false) {
        val dialogView = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(50, 40, 50, 40)
        }

        val hourPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 23
            value = (remainingTimeMillis / 3600000).toInt()
        }

        val minutePicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 59
            value = ((remainingTimeMillis % 3600000) / 60000).toInt()
        }

        val secondPicker = NumberPicker(requireContext()).apply {
            minValue = 0
            maxValue = 59
            value = ((remainingTimeMillis % 60000) / 1000).toInt()
        }

        dialogView.addView(hourPicker)
        dialogView.addView(TextView(requireContext()).apply { 
            text = "h  "
            textSize = 18f
        })
        dialogView.addView(minutePicker)
        dialogView.addView(TextView(requireContext()).apply { 
            text = "m  "
            textSize = 18f
        })
        dialogView.addView(secondPicker)
        dialogView.addView(TextView(requireContext()).apply { 
            text = "s"
            textSize = 18f
        })

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.timer_set_time))
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val hours = hourPicker.value
                val minutes = minutePicker.value
                val seconds = secondPicker.value
                remainingTimeMillis = (hours * 3600000L) + (minutes * 60000L) + (seconds * 1000L)
                initialTimeMillis = remainingTimeMillis
                updateTimeDisplay(remainingTimeMillis)
                
                if (startAfterSet && remainingTimeMillis > 0) {
                    startTimer()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun startTimer() {
        if (remainingTimeMillis <= 0) {
            showTimePickerDialog(startAfterSet = true)
            return
        }
        stopAlarm()
        isRunning = true
        lastTickTime = System.currentTimeMillis()
        btnStartTimer.text = getString(R.string.stopwatch_stop)
        handler.post(updateRunnable)
    }

    private fun pauseTimer() {
        isRunning = false
        btnStartTimer.text = getString(R.string.start)
        handler.removeCallbacks(updateRunnable)
    }

    private fun resetTimer() {
        isRunning = false
        remainingTimeMillis = initialTimeMillis
        btnStartTimer.text = getString(R.string.start)
        handler.removeCallbacks(updateRunnable)
        stopAlarm()
        updateTimeDisplay(remainingTimeMillis)
    }

    private fun updateTimeDisplay(millis: Long) {
        val hours = (millis / 3600000).toInt()
        val minutes = ((millis % 3600000) / 60000).toInt()
        val seconds = ((millis % 60000) / 1000).toInt()

        tvTimerTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun onTimerFinished() {
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            mediaPlayer = MediaPlayer.create(requireContext(), alarmUri)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
        stopAlarm()
    }
}
