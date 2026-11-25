package com.udistrital.parcial_viajes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.parcial_viajes.domain.ExpenseValidator
import com.udistrital.parcial_viajes.model.Category
import com.udistrital.parcial_viajes.model.Expense
import com.udistrital.parcial_viajes.model.NewExpenseInput
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.repository.ApiRepository
import kotlinx.coroutines.launch

class AddExpenseViewModel : ViewModel() {

    var descripcion by mutableStateOf("")
    var monto by mutableStateOf("")
    var categoriaSeleccionada by mutableStateOf<Category?>(null)
    var fecha by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)
    var guardando by mutableStateOf(false)
        private set

    val categorias = mutableStateListOf<Category>()
    var cargandoCategorias by mutableStateOf(false)
        private set

    fun cargarCategorias() {
        viewModelScope.launch {
            cargandoCategorias = true
            runCatching { ApiRepository.listarCategorias() }
                .onSuccess {
                    categorias.clear()
                    categorias.addAll(it)
                }
                .onFailure {
                    mensajeError = "No se pudieron cargar las categorías: ${it.message}"
                }
            cargandoCategorias = false
        }
    }

    fun guardar(trip: Trip, onExito: (Expense) -> Unit) {
        val valor = monto.replace(',', '.').toDoubleOrNull() ?: 0.0
        val input = NewExpenseInput(
            tripId = trip.id,
            description = descripcion.trim(),
            amount = valor,
            categoryId = categoriaSeleccionada?.id ?: 0,
            expenseDate = fecha
        )
        val error = ExpenseValidator.validate(input, trip.startDate, trip.endDate)
        if (error != null) {
            mensajeError = error
            return
        }
        viewModelScope.launch {
            guardando = true
            mensajeError = null
            runCatching { ApiRepository.registrarGasto(input) }
                .onSuccess { gasto ->
                    limpiar()
                    onExito(gasto)
                }
                .onFailure {
                    mensajeError = it.message ?: "No se pudo guardar"
                }
            guardando = false
        }
    }

    fun limpiar() {
        descripcion = ""
        monto = ""
        categoriaSeleccionada = null
        fecha = ""
        mensajeError = null
    }
}
