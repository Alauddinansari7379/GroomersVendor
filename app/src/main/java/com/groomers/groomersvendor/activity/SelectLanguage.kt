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
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class SelectLanguage : Common() {
    private val binding by lazy { ActivitySelectLanguageBinding.inflate(layoutInflater) }
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)

        // Check if language is already selected, if yes, skip this screen
        if (!sessionManager.selectedLanguage.isNullOrEmpty()) {
            navigateToNextScreen()
            return
        }

        // Highlight saved language if available
        sessionManager.selectedLanguage?.let { highlightSelectedButton(it) }

        // Language selection button listeners
        binding.btnEnglish.setOnClickListener { changeLanguage("en", binding.btnEnglish) }
        binding.btnHindi.setOnClickListener { changeLanguage("hi", binding.btnHindi) }
        binding.btnKannada.setOnClickListener { changeLanguage("kn", binding.btnKannada) }

        binding.btnContinue.setOnClickListener {
            if (isLanguageSelected()) {
                navigateToNextScreen()
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

        // Save the selected language in SessionManager
        sessionManager.selectedLanguage = languageCode

        // Update UI to reflect selection
        highlightSelectedButton(languageCode)
    }

    private fun navigateToNextScreen() {
        startActivity(Intent(this@SelectLanguage, Login::class.java))
        finish()
    }

    private fun isLanguageSelected(): Boolean {
        return !sessionManager.selectedLanguage.isNullOrEmpty()
    }

    private fun highlightSelectedButton(languageCode: String) {
        resetButtonBackgrounds()
        when (languageCode) {
            "en" -> binding.btnEnglish.setBackgroundResource(R.drawable.selected_bg)
            "hi" -> binding.btnHindi.setBackgroundResource(R.drawable.selected_bg)
            "kn" -> binding.btnKannada.setBackgroundResource(R.drawable.selected_bg)
        }
    }

    private fun resetButtonBackgrounds() {
        binding.btnEnglish.setBackgroundResource(R.drawable.button_background)
        binding.btnHindi.setBackgroundResource(R.drawable.button_background)
        binding.btnKannada.setBackgroundResource(R.drawable.button_background)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
