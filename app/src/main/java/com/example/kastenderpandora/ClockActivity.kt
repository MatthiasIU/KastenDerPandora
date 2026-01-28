package com.example.kastenderpandora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ClockActivity : BaseToolActivity() {

    override val toolTitle = R.string.clock
    override val toolIcon = R.mipmap.ic_clock
    override val layoutResId = R.layout.activity_clock

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        setupBottomNavigation()
        
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.nav_clock
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_clock -> {
                    loadFragment(ClockFragment())
                    true
                }
                R.id.nav_alarm -> {
                    loadFragment(AlarmFragment())
                    true
                }
                R.id.nav_stopwatch -> {
                    loadFragment(StopwatchFragment())
                    true
                }
                R.id.nav_timer -> {
                    loadFragment(TimerFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
