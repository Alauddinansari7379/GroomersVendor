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

        // Load the saved language
        loadSavedLanguage()

        // Language selection button listeners
        binding.btnEnglish.setOnClickListener {
            changeLanguage("en", binding.btnEnglish)
        }

        binding.btnHindi.setOnClickListener {
            changeLanguage("hi", binding.btnHindi)
        }

        binding.btnKannada.setOnClickListener {
            changeLanguage("kn", binding.btnKannada)
        }

        binding.btnContinue.setOnClickListener {
            // Proceed to the next activity
            startActivity(Intent(this@SelectLanguage, Register2::class.java))
        }
    }

    private fun changeLanguage(languageCode: String, selectedButton: Button) {
        // Set the app's locale to the selected language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Save the selected language to shared preferences
        saveSelectedLanguage(languageCode)

        // Update button backgrounds: Reset all buttons first
        resetButtonBackgrounds()

        // Change the background color of the selected button
        selectedButton.setBackgroundDrawable(resources.getDrawable(R.drawable.selected_bg)) // Green, for example

        // Notify the user
        showToast("Language changed to ${getLanguageName(languageCode)}")
    }

    private fun saveSelectedLanguage(languageCode: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("SelectedLanguage", languageCode)
        editor.apply()
    }

    private fun loadSavedLanguage() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("SelectedLanguage", "en") ?: "en"

        // Apply the saved language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
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

    // Reset the background color of all language buttons
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun resetButtonBackgrounds() {
        binding.btnEnglish.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background))
        binding.btnHindi.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background))
        binding.btnKannada.setBackgroundDrawable(resources.getDrawable(R.drawable.button_background))
    }
}
