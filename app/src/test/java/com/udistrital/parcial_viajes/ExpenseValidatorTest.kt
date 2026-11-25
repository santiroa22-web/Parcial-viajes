package com.udistrital.parcial_viajes

import com.udistrital.parcial_viajes.domain.ExpenseValidator
import com.udistrital.parcial_viajes.model.NewExpenseInput
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ExpenseValidatorTest {

    private val inicio = "2026-06-10"
    private val fin = "2026-06-20"

    @Test
    fun gastoValidoRetornaNull() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 1, "2026-06-12")
        assertNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaDescripcionVacia() {
        val input = NewExpenseInput(1, "", 25000.0, 1, "2026-06-12")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaMontoCero() {
        val input = NewExpenseInput(1, "Cena", 0.0, 1, "2026-06-12")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaMontoNegativo() {
        val input = NewExpenseInput(1, "Cena", -500.0, 1, "2026-06-12")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaCategoriaNoSeleccionada() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 0, "2026-06-12")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaFechaAnteriorAlInicioDelViaje() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 1, "2026-06-05")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun rechazaFechaPosteriorAlFinDelViaje() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 1, "2026-06-25")
        assertNotNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun aceptaFechaEnLaFronteraInicial() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 1, inicio)
        assertNull(ExpenseValidator.validate(input, inicio, fin))
    }

    @Test
    fun aceptaFechaEnLaFronteraFinal() {
        val input = NewExpenseInput(1, "Cena", 25000.0, 1, fin)
        assertNull(ExpenseValidator.validate(input, inicio, fin))
    }
}