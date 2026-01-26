package com.example.kastenderpandora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CounterActivity : BaseToolActivity() {

    override val toolTitle = R.string.counter
    override val layoutResId = R.layout.activity_counter

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tvCount = findViewById<TextView>(R.id.tvCount)
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

        updateCount()
    }
}
