package com.groomers.groomersvendor.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

class MyApplication : Application() {
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate() {
        super.onCreate()
        registerViewModel = ViewModelProvider(
            ViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        ).get(RegisterViewModel::class.java)
    }
}
