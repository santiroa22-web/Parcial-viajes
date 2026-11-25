package com.udistrital.parcial_viajes.domain

import com.udistrital.parcial_viajes.model.Expense

object ExpenseCalculator {

    fun totalForTrip(expenses: List<Expense>, tripId: Int): Double {
        return expenses.filter { it.tripId == tripId }.sumOf { it.amount }
    }

    fun total(expenses: List<Expense>): Double {
        return expenses.sumOf { it.amount }
    }
}
