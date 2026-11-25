package com.udistrital.parcial_viajes.model

enum class AppRoute {
    HOME,
    CREATE_TRIP,
    TRIP_DETAIL,
    ADD_EXPENSE
}

data class Trip(
    val id: Int,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val totalExpenses: Double = 0.0,
    val expenseCount: Int = 0
)

data class Category(
    val id: Int,
    val name: String,
    val description: String = ""
)

data class Expense(
    val id: Int,
    val tripId: Int,
    val description: String,
    val amount: Double,
    val expenseDate: String,
    val categoryId: Int,
    val categoryName: String
)

data class NewTripInput(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class NewExpenseInput(
    val tripId: Int,
    val description: String,
    val amount: Double,
    val categoryId: Int,
    val expenseDate: String
)

data class HomeUiState(
    val currentRoute: AppRoute = AppRoute.HOME,
    val selectedTripId: Int? = null
)
