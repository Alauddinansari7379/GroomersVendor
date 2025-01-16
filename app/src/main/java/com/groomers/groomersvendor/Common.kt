package com.groomers.groomersvendor

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat

open class Common : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Automatically update the status bar color based on the background color of the screen
     */
    fun updateStatusBarColor(color: Int) {
        // Change the status bar color to match the background color
        window.statusBarColor = color

        // Check if the color is white (or very light)
        val isLightColor = color.isLightColor()

        // If the color is light (like white), set the status bar icons to dark
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightColor
    }

    // Extension function to determine if a color is light or dark
    private fun Int.isLightColor(): Boolean {
        val darkness = 1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
        return darkness < 0.5
    }
}