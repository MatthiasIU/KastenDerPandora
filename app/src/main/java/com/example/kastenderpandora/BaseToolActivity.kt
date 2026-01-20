package com.example.kastenderpandora

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseToolActivity : AppCompatActivity() {

    abstract val toolTitle: Int
    open val toolIcon: Int = R.drawable.ic_placeholder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tool_placeholder)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_tool_layout)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        findViewById<TextView>(R.id.tvTitle).setText(toolTitle)
        findViewById<ImageView>(R.id.ivAppIcon).setImageResource(toolIcon)
        findViewById<TextView>(R.id.tvPlaceholder).text = getString(toolTitle)
        
        findViewById<TextView>(R.id.tvBack).setOnClickListener {
            finish()
        }
    }
}