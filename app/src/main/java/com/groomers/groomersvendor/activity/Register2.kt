package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityRegister2Binding

class Register2 : Common() {
    private val binding by lazy { ActivityRegister2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)

        binding.card1.setOnClickListener {
            val intent = Intent(this, About::class.java)
            intent.putExtra("category", "category")
            startActivity(intent)
        }
//        binding.card2.setOnClickListener {
//            startActivity(Intent(this@Register2, Register4::class.java))
//        }
//        binding.card3.setOnClickListener {
//            startActivity(Intent(this@Register2, Register4::class.java))
//        }
//        binding.card4.setOnClickListener {
//            startActivity(Intent(this@Register2, Register4::class.java))
//        }
//        binding.card5.setOnClickListener {
//            startActivity(Intent(this@Register2, Register4::class.java))
//        }
//        binding.card6.setOnClickListener {
//            startActivity(Intent(this@Register2, Register4::class.java))
//        }
    }
}