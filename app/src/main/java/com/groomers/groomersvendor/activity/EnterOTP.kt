package com.groomers.groomersvendor.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.R
import com.groomers.groomersvendor.databinding.ActivityEnterOtpBinding
import com.groomers.groomersvendor.databinding.DialogChangePasswordBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.model.modelForgot.ModelForgot
import com.groomers.groomersvendor.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterOTP : AppCompatActivity() {
    private val context = this@EnterOTP
    var otp = ""
    var email = ""
    var count = 0
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false
    private val binding by lazy { ActivityEnterOtpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOtpInputs(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
        setupOtpBackspaceHandler(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)


        otp = intent.getStringExtra("OTP").toString()
        email = intent.getStringExtra("Email").toString()
        with(binding) {
            btnSubmit.setOnClickListener {
                val enterOtp =
                    binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()+ binding.etOtp5.text.toString()+ binding.etOtp6.text.toString()
                if (enterOtp == otp) {
                    showChangePasswordDialog(context)
                }else{
                    myToast(context, "Entered wrong OTP", false)

                }
            }

        }

    }
    private fun setupOtpBackspaceHandler(vararg editTexts: EditText) {
        for (i in 1 until editTexts.size) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // If deleting text (count == 1, after == 0), and it's the only character
                    if (count == 1 && after == 0 && s?.length == 1) {
                        editTexts[i - 1].requestFocus()
                        editTexts[i - 1].setSelection(editTexts[i - 1].text.length)
                    }
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setupOtpInputs(vararg editTexts: EditText) {
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener {
                if (editTexts[i].text.length == 1 && i < editTexts.size - 1) {
                    editTexts[i + 1].requestFocus()
                }
            }
        }
    }
   private fun showChangePasswordDialog(context: Context) {
        val binding = DialogChangePasswordBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(true)
            .create()
       binding.ivTogglePassword.setOnClickListener {
           isPasswordVisible = !isPasswordVisible
           togglePasswordVisibility(binding.etPassword, binding.ivTogglePassword, isPasswordVisible)
       }

       binding.ivToggleConfirmPassword.setOnClickListener {
           isConfirmPasswordVisible = !isConfirmPasswordVisible
           togglePasswordVisibility(binding.etConfirmPassword, binding.ivToggleConfirmPassword, isConfirmPasswordVisible)
       }
        binding.btnSubmit.setOnClickListener {
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            when {
                password.isEmpty() || confirmPassword.isEmpty() -> {
                    myToast(context, "Please fill all fields", false)
                }
                password.length < 8 -> {
                    myToast(context, "Password must be at least 8 characters long", false)
                }
                password != confirmPassword -> {
                    myToast(context, "Passwords do not match", false)
                }
                else -> {
                    apiCallResetPass(confirmPassword)
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }


    private fun apiCallResetPass(confirmPassword: String) {
        CustomLoader.showLoaderDialog(context)
        ApiClient.apiService.resetPassword(email, "vendor", confirmPassword)
            .enqueue(object : Callback<ModelForgot> {
                override fun onResponse(
                    call: Call<ModelForgot>, response: Response<ModelForgot>
                ) {
                    try {
                        CustomLoader.hideLoaderDialog()
                        when (response.code()) {
                            404 -> myToast(context, "Something went wrong", false)
                            500 -> myToast(context, "Server Error", false)
                            else -> {
                                myToast(context, "Password changed successfully", true)
                                val intent = Intent(context, Login::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)


                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong", false)
                        CustomLoader.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelForgot>, t: Throwable) {
                    CustomLoader.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallResetPass(confirmPassword)
                    } else {
                        myToast(context, t.message.toString(), false)
                        CustomLoader.hideLoaderDialog()
                    }
                }
            })
    }


    private fun togglePasswordVisibility(editText: EditText, icon: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            icon.setImageResource(R.drawable.visibility_on)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            icon.setImageResource(R.drawable.visibility_off)
        }
        editText.setSelection(editText.text.length) // Move cursor to end
    }

}