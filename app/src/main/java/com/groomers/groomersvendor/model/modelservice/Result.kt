package com.groomers.groomersvendor.model.modelservice

data class Result(
    val address: String,
    val category: String,
    val created_at: String,
    val date: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Int,
    val serviceName: String,
    val serviceType: String,
    val slot_time: String,
    val status: Int,
    val time: String,
    val updated_at: String,
    val user_id: Int,
    val user_type: String
)