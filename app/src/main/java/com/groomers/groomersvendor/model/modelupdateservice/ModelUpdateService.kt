package com.groomers.groomersvendor.model.modelupdateservice

data class ModelUpdateService(
    val message: String,
    val result: Result,
    val slot_results: List<String>,
    val status: Int
)