package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.databinding.ActivityRegister4Binding
import com.groomers.groomersvendor.viewmodel.MyApplication

class Register4 : Common() {
    private val binding by lazy { ActivityRegister4Binding.inflate(layoutInflater) }
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE
        updateStatusBarColor(backgroundColor)


        binding.btnContinue3.setOnClickListener {
            val selectedId = binding.rgTeamSize.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val teamSize = selectedRadioButton.text.toString()
                viewModel.teamSize = teamSize
                val intent = Intent(this, YourAddress::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please select a team size", Toast.LENGTH_SHORT).show()
            }
        }

    }
}