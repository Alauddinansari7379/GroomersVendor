package com.groomers.groomersvendor.model.modelhelplist

data class ModelHelpList(
    val message: String,
    val result: List<Result>,
    val status: Int
)