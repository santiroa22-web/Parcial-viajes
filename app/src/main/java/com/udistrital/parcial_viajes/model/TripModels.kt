package com.udistrital.parcial_viajes.model

enum class AppRoute {
    HOME,
    CREATE_TRIP,
    TRIP_DETAIL,
    ADD_EXPENSE
}

data class TripUiItem(
    val name: String,
    val destination: String
)

data class ExpenseUiItem(
    val category: String,
    val description: String,
    val date: String,
    val amount: String
)

data class HomeUiState(
    val currentRoute: AppRoute = AppRoute.HOME,
    val selectedTripTitle: String = "Europa 2026"
)