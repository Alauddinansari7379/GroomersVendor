package com.groomers.groomersvendor.model.modellogin

data class ModelLogin(
    val access_token: String,
    val expires_in: Int,
    val token_type: String,
    val user: User
)