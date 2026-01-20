package com.example.kastenderpandora

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    data class Tool(val nameRes: Int, val activityClass: Class<*>)

    private val tools = listOf(
        Tool(R.string.light, LightActivity::class.java),
        Tool(R.string.ruler, RulerActivity::class.java),
        Tool(R.string.counter, CounterActivity::class.java),
        Tool(R.string.clock, ClockActivity::class.java),
        Tool(R.string.calculator, CalculatorActivity::class.java),
        Tool(R.string.protractor, ProtractorActivity::class.java),
        Tool(R.string.spirit_level, SpiritLevelActivity::class.java),
        Tool(R.string.decibel_meter, DecibelMeterActivity::class.java),
        Tool(R.string.compass, CompassActivity::class.java),
        Tool(R.string.siren, SirenActivity::class.java),
        Tool(R.string.magnifying_glass, MagnifyingGlassActivity::class.java),
        Tool(R.string.voice_recorder, VoiceRecorderActivity::class.java),
        Tool(R.string.lumen_meter, LumenMeterActivity::class.java),
        Tool(R.string.camera, CameraActivity::class.java),
        Tool(R.string.audio_spectrum, AudioSpectrumActivity::class.java)
    )

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

        val inflater = LayoutInflater.from(this)
        val iconSize = calculateDynamicIconSize(columns)

        tools.forEach { tool ->
            val itemView = inflater.inflate(R.layout.grid_item_tool, grid, false)
            
            val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
            val tvLabel = itemView.findViewById<TextView>(R.id.tvLabel)
            
            ivIcon.layoutParams.width = iconSize
            ivIcon.layoutParams.height = iconSize
            ivIcon.setImageResource(R.drawable.ic_placeholder)
            
            tvLabel.setText(tool.nameRes)
            
            itemView.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            
            itemView.setOnClickListener {
                startActivity(Intent(this, tool.activityClass))
            }
            
            grid.addView(itemView)
        }

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun calculateDynamicIconSize(columns: Int): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val padding = (32 * displayMetrics.density).toInt() // Total horizontal padding in activity_main + grid margins
        val availableWidth = screenWidth - padding
        val itemWidth = availableWidth / columns
        
        // Icon should be about 60% of the item width, but capped at a reasonable size (e.g. 96dp)
        val maxIconSize = (96 * displayMetrics.density).toInt()
        val targetSize = (itemWidth * 0.6).toInt()
        
        return Math.min(targetSize, maxIconSize)
    }
}
