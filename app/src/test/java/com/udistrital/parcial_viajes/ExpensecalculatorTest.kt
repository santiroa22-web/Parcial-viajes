package com.udistrital.parcial_viajes

import com.udistrital.parcial_viajes.domain.ExpenseCalculator
import com.udistrital.parcial_viajes.model.Expense
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpenseCalculatorTest {

    private val gastos = listOf(
        Expense(1, 1, "Cena", 25000.0, "2026-06-11", 1, "Alimentación"),
        Expense(2, 1, "Hotel", 180000.0, "2026-06-12", 2, "Alojamiento"),
        Expense(3, 2, "Taxi", 15000.0, "2026-07-02", 3, "Transporte"),
        Expense(4, 1, "Tour", 90000.0, "2026-06-13", 4, "Actividades")
    )

    @Test
    fun totalListaVaciaEsCero() {
        assertEquals(0.0, ExpenseCalculator.total(emptyList()), 0.0001)
    }

    @Test
    fun totalSumaTodosLosGastos() {
        assertEquals(310000.0, ExpenseCalculator.total(gastos), 0.0001)
    }

    @Test
    fun totalPorViajeSoloSumaGastosDelViaje() {
        assertEquals(295000.0, ExpenseCalculator.totalForTrip(gastos, 1), 0.0001)
        assertEquals(15000.0, ExpenseCalculator.totalForTrip(gastos, 2), 0.0001)
    }

    @Test
    fun totalPorViajeInexistenteEsCero() {
        assertEquals(0.0, ExpenseCalculator.totalForTrip(gastos, 99), 0.0001)
    }
}