package com.groomers.groomersvendor.model.modelEarning

data class Result(
    val earnings: List<Earning>,
    val monthly_earnings: Int,
    val today_earnings: Int,
    val total_earnings: Double,
    val weekly_earnings: Int
)