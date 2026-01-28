package com.example.kastenderpandora

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockActivity : BaseToolActivity() {

    override val toolTitle = R.string.clock
    override val toolIcon = R.mipmap.ic_clock
    override val layoutResId = R.layout.activity_clock

    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView
    private lateinit var handler: Handler
    private lateinit var timeRunnable: Runnable
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvTime = findViewById(R.id.tvTime)
        tvDate = findViewById(R.id.tvDate)
        
        handler = Handler(Looper.getMainLooper())
        timeRunnable = object : Runnable {
            override fun run() {
                updateTime()
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(timeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRunnable)
    }

    private fun updateTime() {
        val now = Date()
        tvTime.text = timeFormat.format(now)
        tvDate.text = dateFormat.format(now)
    }
}
