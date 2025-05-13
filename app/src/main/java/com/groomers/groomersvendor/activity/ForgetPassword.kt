package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ehcf.Helper.myToast
import com.groomers.groomersvendor.databinding.ActivityForgetPasswordBinding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.model.modelForgot.ModelForgot
import com.groomers.groomersvendor.retrofit.ApiClient
import com.groomers.groomersvendor.retrofit.ApiService
import com.groomers.groomersvendor.sharedpreferences.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ForgetPassword : AppCompatActivity() {
    private val context = this@ForgetPassword

    @Inject
    lateinit var apiService: ApiService
    var count = 0
    var otp = ""

    @Inject
    lateinit var sessionManager: SessionManager
    private val binding by lazy { ActivityForgetPasswordBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            btnSendOtp.setOnClickListener {
                if (binding.etEmail.text.toString().isEmpty()) {
                    myToast(context, "Enter email", false)
                    binding.etEmail.requestFocus()
                    return@setOnClickListener
                }
                apiCallSendOTP()
            }


            tvRegister.setOnClickListener {
                startActivity(Intent(context, Register::class.java))
            }
        }
    }

    private fun apiCallSendOTP() {
        CustomLoader.showLoaderDialog(context)
        ApiClient.apiService.forgotPassword(binding.etEmail.text.toString().trim(), "vendor")
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
                                myToast(context, "OTP sent successfully", true)
                                otp = response.body()?.result!!.otp.toString()
                                val intent = Intent(context, EnterOTP::class.java)
                                intent.putExtra("OTP", otp)
                                intent.putExtra("Email", binding.etEmail.text.toString().trim())
                                context.startActivity(intent)


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
                        apiCallSendOTP()
                    } else {
                        myToast(context, t.message.toString(), false)
                        CustomLoader.hideLoaderDialog()
                    }
                }
            })
    }

}