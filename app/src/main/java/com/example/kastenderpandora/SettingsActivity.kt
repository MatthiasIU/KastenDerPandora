package com.example.kastenderpandora

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kastenderpandora.i18n.LanguageManager
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private var currentLanguage: String? = null
    private var currentDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        currentLanguage = LanguageManager.getCurrentLanguage(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setupLanguageSelector()
        setupDarkModeSwitch()
        setupSaveButton()
        setupResetButton()
        setupBackButton()
    }

    private fun setupLanguageSelector() {
        val languageSpinner = findViewById<Spinner>(R.id.spinnerLanguage)
        val languages = LanguageManager.getSupportedLanguages()

        android.util.Log.d("SettingsActivity", "Languages: ${languages.map { "${it.code} (${it.displayName})" }}")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            languages.map { it.displayName }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        currentLanguage?.let { lang ->
            val currentLanguageIndex = languages.indexOfFirst { it.code == lang }
            android.util.Log.d("SettingsActivity", "Setting initial selection to $lang at index $currentLanguageIndex")
            if (currentLanguageIndex >= 0) {
                languageSpinner.setSelection(currentLanguageIndex, false)
            }
        }

        languageSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                android.util.Log.d("SettingsActivity", "Selected language: ${selectedLanguage.code}, Position: $position")
                currentLanguage = selectedLanguage.code
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                android.util.Log.d("SettingsActivity", "Nothing selected")
            }
        }
    }

    private fun setupDarkModeSwitch() {
        val prefs = getSharedPreferences(PandoraApplication.PREFS_NAME, MODE_PRIVATE)
        val sw = findViewById<SwitchMaterial>(R.id.switchDarkMode)

        currentDarkMode = prefs.getBoolean(PandoraApplication.KEY_DARKMODE, false)
        sw.isChecked = currentDarkMode

        sw.setOnCheckedChangeListener { _, isChecked ->
            currentDarkMode = isChecked
        }
    }

    private fun saveSettings() {
        val prefs = getSharedPreferences(PandoraApplication.PREFS_NAME, MODE_PRIVATE)
        prefs.edit(commit = true) {
            if (currentLanguage == null) remove(PandoraApplication.KEY_LANGUAGE)
            else putString(PandoraApplication.KEY_LANGUAGE, currentLanguage)

            putBoolean(PandoraApplication.KEY_DARKMODE, currentDarkMode)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (currentDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }


    private fun setupSaveButton() {
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveSettings()
            restartToMain()
        }
    }

    private fun setupResetButton() {
        findViewById<Button>(R.id.btnReset).setOnClickListener {
            currentLanguage = null
            currentDarkMode = false
            saveSettings()
            restartToMain()
        }
    }

    private fun setupBackButton() {
        val back = findViewById<TextView>(R.id.tvBack)
        back.setOnClickListener {
            finish()
        }
    }

    private fun changeLanguage(languageCode: String) {
        android.util.Log.d("SettingsActivity", "Changing language to: $languageCode")
        
        val prefs = getSharedPreferences(PandoraApplication.PREFS_NAME, MODE_PRIVATE)
        android.util.Log.d("SettingsActivity", "Before save - Language in prefs: ${prefs.getString(PandoraApplication.KEY_LANGUAGE, "null")}")
        prefs.edit(commit = true) { putString(PandoraApplication.KEY_LANGUAGE, languageCode) }
        android.util.Log.d("SettingsActivity", "After save - Language in prefs: ${prefs.getString(PandoraApplication.KEY_LANGUAGE, "null")}")
        
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        
        android.util.Log.d("SettingsActivity", "Language change complete, restarting app")
    }

    private fun restartToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
