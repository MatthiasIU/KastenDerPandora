package com.example.kastenderpandora

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val grid = findViewById<GridLayout>(R.id.grid)
        val settings = findViewById<TextView>(R.id.tvSettings)

        val columns = UiConfig.getHomeGridColumns(this)
        grid.columnCount = columns

        repeat(8) { index ->
            val btn = MaterialButton(this).apply {
                text = "App ${index + 1}"
                icon = getDrawable(R.mipmap.ic_launcher)
                iconSize = 48
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_TOP
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = resources.getDimensionPixelSize(R.dimen.grid_item_height)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
            }
            grid.addView(btn)
        }

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
