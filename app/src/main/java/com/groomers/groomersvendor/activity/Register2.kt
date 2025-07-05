package com.groomers.groomersvendor.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.groomers.groomersvendor.Common
import com.groomers.groomersvendor.MainActivity
import com.groomers.groomersvendor.adapter.CategoryAdapter
import com.groomers.groomersvendor.adapter.CategoryAdapter.Companion.categoryId
import com.groomers.groomersvendor.adapter.OthersCategoryAdapter
import com.groomers.groomersvendor.databinding.ActivityRegister2Binding
import com.groomers.groomersvendor.helper.CustomLoader
import com.groomers.groomersvendor.retrofit.ApiServiceProvider
import com.groomers.groomersvendor.viewmodel.CategoryViewModel
import com.groomers.groomersvendor.viewmodel.MyApplication
import com.groomers.groomersvendor.viewmodel.RegisterViewModel

class Register2 : Common() {
    private val binding by lazy { ActivityRegister2Binding.inflate(layoutInflater) }
    private val viewModel by lazy {
        (application as MyApplication).registerViewModel
    }
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val context = this@Register2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Get the background color of the root view
        val backgroundColor = (binding.root.background as? ColorDrawable)?.color ?: Color.WHITE

        // Update the status bar color to match the background color
        updateStatusBarColor(backgroundColor)
        val apiService = ApiServiceProvider.getApiService()
        categoryViewModel.getCategory(apiService)
        // Observe isLoading to show/hide progress
        categoryViewModel.isLoading.observe(context) { isLoading ->
            if (isLoading) {
                CustomLoader.showLoaderDialog(context)
            } else {
                CustomLoader.hideLoaderDialog()
            }
        }
        // Observe error message if login fails
        categoryViewModel.errorMessage.observe(context) { errorMessage ->
            if (errorMessage != null) {
                if (errorMessage.isNotEmpty()) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

//        binding.card1.setOnClickListener {
//            val intent = Intent(this, About::class.java)
//            startActivity(intent)
//        }
        categoryViewModel.modelCategory.observe(context) { modelCategory ->
            binding.rvCategory.apply {
                adapter = OthersCategoryAdapter(modelCategory!!.result, context)
            }
            val gridLayoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
            binding.rvCategory1.layoutManager = gridLayoutManager
            binding.rvCategory1.adapter = CategoryAdapter(modelCategory!!.result, context)

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