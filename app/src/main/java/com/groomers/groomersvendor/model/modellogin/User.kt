package com.groomers.groomersvendor.model.modellogin

data class User(
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val language: String,
    val mobile: String,
    val name: String,
    val role: String,
    val status: Int,
    val updated_at: String,
    val VendorStatus: Int,
    val username: String
)