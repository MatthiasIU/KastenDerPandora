package com.example.kastenderpandora

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class StopwatchFragment : Fragment() {

    private lateinit var tvStopwatchTime: TextView
    private lateinit var btnStartStop: Button
    private lateinit var btnReset: Button

    private var isRunning = false
    private var elapsedTimeMillis = 0L
    private var startTimeMillis = 0L

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                val totalElapsed = elapsedTimeMillis + (currentTime - startTimeMillis)
                updateTimeDisplay(totalElapsed)
                handler.postDelayed(this, 10) // Update every 10ms for smooth display
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvStopwatchTime = view.findViewById(R.id.tvStopwatchTime)
        btnStartStop = view.findViewById(R.id.btnStartStop)
        btnReset = view.findViewById(R.id.btnReset)

        btnStartStop.setOnClickListener {
            if (isRunning) {
                stopStopwatch()
            } else {
                startStopwatch()
            }
        }

        btnReset.setOnClickListener {
            resetStopwatch()
        }

        // Initialize display
        updateTimeDisplay(elapsedTimeMillis)
    }

    private fun startStopwatch() {
        isRunning = true
        startTimeMillis = System.currentTimeMillis()
        btnStartStop.text = getString(R.string.stopwatch_stop)
        handler.post(updateRunnable)
    }

    private fun stopStopwatch() {
        isRunning = false
        elapsedTimeMillis += System.currentTimeMillis() - startTimeMillis
        btnStartStop.text = getString(R.string.start)
        handler.removeCallbacks(updateRunnable)
    }

    private fun resetStopwatch() {
        isRunning = false
        elapsedTimeMillis = 0L
        startTimeMillis = 0L
        btnStartStop.text = getString(R.string.start)
        handler.removeCallbacks(updateRunnable)
        updateTimeDisplay(0L)
    }

    private fun updateTimeDisplay(millis: Long) {
        val hours = (millis / 3600000).toInt()
        val minutes = ((millis % 3600000) / 60000).toInt()
        val seconds = ((millis % 60000) / 1000).toInt()
        val centiseconds = ((millis % 1000) / 10).toInt()

        tvStopwatchTime.text = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateRunnable)
    }
}
