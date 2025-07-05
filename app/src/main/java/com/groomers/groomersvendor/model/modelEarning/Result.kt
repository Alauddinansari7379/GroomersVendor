package com.groomers.groomersvendor.model.modelEarning

data class Result(
    val earnings: List<Earning>,
    val today_order: List<Earning>,
    val weekly_order: List<Earning>,
    val monthly_order: List<Earning>,
    val monthly_earnings: Float,
    val today_earnings: Float,
    val total_earnings: Float,
    val weekly_earnings: Float
)