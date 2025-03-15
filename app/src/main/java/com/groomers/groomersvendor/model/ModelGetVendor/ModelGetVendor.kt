package com.groomers.groomersvendor.model.ModelGetVendor

data class ModelGetVendor(
    val message: String,
    val result: List<Result>,
    val status: Int
)