package com.groomers.groomersvendor.model.modelGetBooking

data class ModelGetBooking(
    val message: String,
    val result: List<Result>,
    val status: Int
)