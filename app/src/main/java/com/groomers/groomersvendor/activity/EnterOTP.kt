package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.databinding.ActivityEnterOtpBinding
import com.groomers.groomersvendor.databinding.ActivityForgetPasswordBinding
import com.groomers.groomersvendor.helper.AppProgressBar
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
    private val binding by lazy { ActivityEnterOtpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOtpInputs(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4,binding.etOtp5,binding.etOtp6)


        otp = intent.getStringExtra("OTP").toString()
        email = intent.getStringExtra("Email").toString()
        with(binding) {
            btnSubmit.setOnClickListener {
                val enterOtp =
                    binding.etOtp1.text.toString() + binding.etOtp2.text.toString() + binding.etOtp3.text.toString() + binding.etOtp4.text.toString()+ binding.etOtp5.text.toString()+ binding.etOtp6.text.toString()
                if (enterOtp == otp) {
                    myToast(context, "Success", true)
                }else{
                    myToast(context, "Entered wrong OTP", false)

                }
            }

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

    private fun apiCallResetPass() {
        ApiClient.apiService.resetPassword(email, "vendor", "")
            .enqueue(object : Callback<ModelForgot> {
                override fun onResponse(
                    call: Call<ModelForgot>, response: Response<ModelForgot>
                ) {
                    try {
                        AppProgressBar.hideLoaderDialog()
                        when (response.code()) {
                            404 -> myToast(context, "Something went wrong", false)
                            500 -> myToast(context, "Server Error", false)
                            else -> {
                                myToast(context, "Success", false)

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        myToast(context, "Something went wrong", false)
                        AppProgressBar.hideLoaderDialog()
                    }
                }

                override fun onFailure(call: Call<ModelForgot>, t: Throwable) {
                    AppProgressBar.hideLoaderDialog()
                    count++
                    if (count <= 3) {
                        Log.e("count", count.toString())
                        apiCallResetPass()
                    } else {
                        myToast(context, t.message.toString(), false)
                        AppProgressBar.hideLoaderDialog()
                    }
                }
            })
    }


}