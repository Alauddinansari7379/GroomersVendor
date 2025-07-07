package com.groomers.groomersvendor.model.modelEarning

data class Earning(
    val amount: Double,
    val booking_id: String,
    val created_at: String,
    val customer_id: String,
    val id: Int,
    val updated_at: String,
    val name: String,
    val serviceName: String,
    val vendor_id: Int
)