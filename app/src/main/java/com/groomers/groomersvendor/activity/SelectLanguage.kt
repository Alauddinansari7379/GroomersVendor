package com.groomers.groomersvendor.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivitySelectLanguageBinding
import java.util.Locale
class SelectLanguage : Common() {
    private val binding by lazy { ActivitySelectLanguageBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)

        // Load and apply the saved language
        val savedLanguage = loadSavedLanguage()
        highlightSelectedButton(savedLanguage)

        // Language selection button listeners
        binding.btnEnglish.setOnClickListener { changeLanguage("en", binding.btnEnglish) }
        binding.btnHindi.setOnClickListener { changeLanguage("hi", binding.btnHindi) }
        binding.btnKannada.setOnClickListener { changeLanguage("kn", binding.btnKannada) }

        binding.btnContinue.setOnClickListener {
            if (isLanguageSelected()) {
                // Proceed to the next activity
                startActivity(Intent(this@SelectLanguage, Login::class.java))
            } else {
                showToast("Please select a language before continuing.")
            }
        }
    }

    private fun changeLanguage(languageCode: String, selectedButton: Button) {
        if (languageCode.isBlank()) {
            showToast("Invalid language selection.")
            return
        }

        // Set the app's locale to the selected language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config) // Ensures the configuration is properly applied

        // Save the selected language
        saveSelectedLanguage(languageCode)

        // Update button backgrounds
        highlightSelectedButton(languageCode)

        // Notify the user
        showToast("Language changed to ${getLanguageName(languageCode)}")
    }

    private fun saveSelectedLanguage(languageCode: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SelectedLanguage", languageCode).apply()
    }

    private fun loadSavedLanguage(): String {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("SelectedLanguage", "en") ?: "en"
    }

    private fun getLanguageName(languageCode: String): String {
        return when (languageCode) {
            "en" -> "English"
            "hi" -> "Hindi"
            "kn" -> "Kannada"
            else -> "Unknown"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isLanguageSelected(): Boolean {
        val selectedLanguage = loadSavedLanguage()
        return selectedLanguage.isNotEmpty() && selectedLanguage in listOf("en", "hi", "kn")
    }

    private fun highlightSelectedButton(languageCode: String) {
        resetButtonBackgrounds()
        when (languageCode) {
            "en" -> binding.btnEnglish.setBackgroundResource(R.drawable.selected_bg)
            "hi" -> binding.btnHindi.setBackgroundResource(R.drawable.selected_bg)
            "kn" -> binding.btnKannada.setBackgroundResource(R.drawable.selected_bg)
        }
    }

    // Reset the background color of all language buttons
    private fun resetButtonBackgrounds() {
        binding.btnEnglish.setBackgroundResource(R.drawable.button_background)
        binding.btnHindi.setBackgroundResource(R.drawable.button_background)
        binding.btnKannada.setBackgroundResource(R.drawable.button_background)
    }
}
