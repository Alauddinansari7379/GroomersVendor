package com.groomers.groomersvendor.model.modelstate

data class ModelState(
    val message: String,
    val result: List<Result>,
    val status: Int
)