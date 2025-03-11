package com.groomers.groomersvendor.model.modelhelplist

data class Result(
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val mobile: Long,
    val name: String,
    val query: String,
    val querystatus: String,
    val status: Int,
    val updated_at: String,
    val user_id: Int
)