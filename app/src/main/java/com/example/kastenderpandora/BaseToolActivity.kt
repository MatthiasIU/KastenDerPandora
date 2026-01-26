package com.example.kastenderpandora

import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kastenderpandora.utils.SwipeGestureDetector

abstract class BaseToolActivity : AppCompatActivity() {

    abstract val toolTitle: Int
    open val toolIcon: Int = R.drawable.ic_placeholder
    open val layoutResId: Int = R.layout.activity_tool_placeholder
    open val showBackButton: Boolean = true
    open val enableSwipeBack: Boolean = false

    private var swipeGestureDetector: SwipeGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutResId)

        setupWindowInsets()
        setupHeader()
        setupBackButton()
        setupSwipeGestures()
        setupToolContent()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        swipeGestureDetector?.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun setupWindowInsets() {
        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main_tool_layout)?.let { layout ->
            ViewCompat.setOnApplyWindowInsetsListener(layout) { v, insets ->
                val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
                insets
            }
        }
    }

    private fun setupHeader() {
        findViewById<TextView>(R.id.tvTitle)?.setText(toolTitle)
        findViewById<ImageView>(R.id.ivAppIcon)?.setImageResource(toolIcon)
    }

    private fun setupBackButton() {
        val backButton = findViewById<TextView>(R.id.tvBack)
        if (showBackButton && backButton != null) {
            backButton.visibility = android.view.View.VISIBLE
            backButton.setOnClickListener {
                onBackButtonPressed()
            }
        } else {
            backButton?.visibility = android.view.View.GONE
        }
    }

    private fun setupSwipeGestures() {
        if (enableSwipeBack) {
            swipeGestureDetector = SwipeGestureDetector(
                this,
                onSwipeLeft = { onBackButtonPressed() }
            )
        }
    }

    protected open fun onBackButtonPressed() {
        finish()
    }

    protected open fun setupToolContent() {
        findViewById<TextView>(R.id.tvPlaceholder)?.text = getString(toolTitle)
    }
}