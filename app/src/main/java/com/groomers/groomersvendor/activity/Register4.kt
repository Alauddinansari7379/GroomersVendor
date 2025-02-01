package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityRegister4Binding

class Register4 : Common() {
    private val binding by lazy { ActivityRegister4Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)

        val category = intent.getStringExtra("category")
        val businessName = intent.getStringExtra("businessName")
        val yourName = intent.getStringExtra("yourName")
        val phoneNO = intent.getStringExtra("phoneNO")
        val userId = intent.getStringExtra("userId")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        binding.btnContinue3.setOnClickListener {
            val selectedId = binding.rgTeamSize.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val teamSize = selectedRadioButton.text.toString()
                val intent = Intent(this, YourAddress::class.java)
                intent.putExtra("category", category)
                intent.putExtra("businessName", businessName)
                intent.putExtra("yourName", yourName)
                intent.putExtra("phoneNO", phoneNO)
                intent.putExtra("userId", userId)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                intent.putExtra("teamSize", teamSize)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please select a team size", Toast.LENGTH_SHORT).show()
            }
        }

    }
}