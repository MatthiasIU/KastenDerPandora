package com.example.kastenderpandora

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import com.example.kastenderpandora.ui.ClickableTextView

class CounterActivity : BaseToolActivity() {

    override val toolTitle = R.string.counter
    override val layoutResId = R.layout.activity_counter
    override val enableSwipeBack = true

    private var count = 0
    private var doubleTapDetector: GestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvCount = findViewById<ClickableTextView>(R.id.tvCount)
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnMinus = findViewById<Button>(R.id.btnMinus)
        val btnReset = findViewById<Button>(R.id.btnReset)

        fun updateCount() {
            tvCount.text = count.toString()
        }

        btnPlus.setOnClickListener {
            count++
            updateCount()
        }

        btnMinus.setOnClickListener {
            count--
            updateCount()
        }

        btnReset.setOnClickListener {
            count = 0
            updateCount()
        }

        tvCount.isClickable = true

        tvCount.setOnTouchListener { view, event ->
            val handled = doubleTapDetector?.onTouchEvent(event) ?: false
            if (!handled && event.action == MotionEvent.ACTION_UP) {
                view.performClick()
            }
            handled
        }

        doubleTapDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                tvCount.performClick()
                count = 0
                updateCount()
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                tvCount.performClick()
                return true
            }
        })

        updateCount()
    }
}
