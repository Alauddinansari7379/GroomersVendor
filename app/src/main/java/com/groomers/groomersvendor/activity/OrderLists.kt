package com.groomers.groomersvendor.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.groomers.groomersvendor.databinding.ActivityOrderListsBinding

class OrderLists : AppCompatActivity() {
    private val binding by lazy { ActivityOrderListsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.llmain.setOnClickListener {
            startActivity(Intent(this@OrderLists,OrderDetail::class.java))
        }

    }
}