package com.groomers.groomersvendor.model

data class ModelDay(val day: String, val id: String, var isSelected: Boolean = false) {
    override fun toString(): String {
        return day // Ensures the spinner shows just the day name
    }
}
