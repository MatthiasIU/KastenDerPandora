package com.example.kastenderpandora

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    data class Tool(val nameRes: Int, val activityClass: Class<*>, val icon: Int)

    private val tools = listOf(
        Tool(R.string.light, LightActivity::class.java, R.mipmap.ic_light),
        Tool(R.string.ruler, RulerActivity::class.java, R.mipmap.ic_ruler),
        Tool(R.string.counter, CounterActivity::class.java, R.mipmap.ic_counter),
        Tool(R.string.clock, ClockActivity::class.java, R.mipmap.ic_clock),
        Tool(R.string.calculator, CalculatorActivity::class.java, R.mipmap.ic_calculator),
        Tool(R.string.protractor, ProtractorActivity::class.java, R.mipmap.ic_protractor),
        Tool(R.string.spirit_level, SpiritLevelActivity::class.java, R.mipmap.ic_spirit_level),
        Tool(R.string.decibel_meter, DecibelMeterActivity::class.java, R.mipmap.ic_decibel_meter),
        Tool(R.string.compass, CompassActivity::class.java, R.mipmap.ic_compass),
        Tool(R.string.siren, SirenActivity::class.java, R.mipmap.ic_siren),
        Tool(R.string.magnifying_glass, MagnifyingGlassActivity::class.java, R.mipmap.ic_magnifying_glass),
        Tool(R.string.voice_recorder, VoiceRecorderActivity::class.java, R.mipmap.ic_voice_recorder),
        Tool(R.string.lumen_meter, LumenMeterActivity::class.java, R.drawable.ic_placeholder),
        Tool(R.string.camera, CameraActivity::class.java, R.drawable.ic_placeholder),
        Tool(R.string.audio_spectrum, AudioSpectrumActivity::class.java, R.drawable.ic_placeholder)
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

        grid.removeAllViews() // sicherheitshalber

        val inflater = LayoutInflater.from(this)

        grid.post {
            val gapPx = dpToPx(8f)
            val iconSize = calculateDynamicIconSize(grid, columns, gapPx)

            tools.forEach { tool ->
                val itemView = inflater.inflate(R.layout.grid_item_tool, grid, false)

                val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
                val tvLabel = itemView.findViewById<TextView>(R.id.tvLabel)

                ivIcon.layoutParams.width = iconSize
                ivIcon.layoutParams.height = iconSize
                ivIcon.setImageResource(tool.icon)

                tvLabel.setText(tool.nameRes)
                tvLabel.textSize = if (columns >= 5) 11f else 12f

                itemView.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(gapPx / 2, gapPx / 2, gapPx / 2, gapPx / 2)
                }

                itemView.setOnClickListener {
                    startActivity(Intent(this, tool.activityClass))
                }

                grid.addView(itemView)
            }
        }


        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(PandoraApplication.PREFS_NAME, MODE_PRIVATE)
        val savedLanguage = prefs.getString(PandoraApplication.KEY_LANGUAGE, null)

        if (savedLanguage != null) {
            val config = Configuration(newBase.resources.configuration)
            val locale = Locale.forLanguageTag(savedLanguage)
            config.setLocale(locale)

            val newContext = newBase.createConfigurationContext(config)
            super.attachBaseContext(newContext)
        } else {
            super.attachBaseContext(newBase)
        }
    }

    private fun calculateDynamicIconSize(grid: GridLayout, columns: Int, gapPx: Int): Int {
        val availableWidth = grid.width - grid.paddingLeft - grid.paddingRight - (gapPx * (columns - 1))
        val itemWidth = (availableWidth / columns).coerceAtLeast(1)

        val target = (itemWidth * 0.60f).toInt()
        val min = dpToPx(48f)
        val max = dpToPx(96f)

        return target.coerceIn(min, max)
    }

    private fun dpToPx(dp: Float): Int {
        val dm = resources.displayMetrics
        return (dp * dm.density + 0.5f).toInt()
    }

}