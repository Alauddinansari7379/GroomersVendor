package com.groomers.groomersvendor.model.modelEarning

data class Result(
    val earnings: List<Earning>,
    val today_order: List<Earning>,
    val weekly_order: List<Earning>,
    val monthly_order: List<Earning>,
    val monthly_earnings: Double,
    val today_earnings: Double,
    val total_earnings: Double,
    val weekly_earnings: Double
)