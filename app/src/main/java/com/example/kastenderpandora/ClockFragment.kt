package com.example.kastenderpandora

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockFragment : Fragment() {

    private lateinit var tvTime: TextView
    private lateinit var tvDate: TextView
    private lateinit var handler: Handler
    private lateinit var timeRunnable: Runnable
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTime = view.findViewById(R.id.tvTime)
        tvDate = view.findViewById(R.id.tvDate)
        
        handler = Handler(Looper.getMainLooper())
        timeRunnable = object : Runnable {
            override fun run() {
                updateTime()
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(timeRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(timeRunnable)
    }

    private fun updateTime() {
        val now = Date()
        tvTime.text = timeFormat.format(now)
        tvDate.text = dateFormat.format(now)
    }
}