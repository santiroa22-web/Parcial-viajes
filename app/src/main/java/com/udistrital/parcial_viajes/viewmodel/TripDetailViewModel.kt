package com.udistrital.parcial_viajes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.parcial_viajes.domain.ExpenseCalculator
import com.udistrital.parcial_viajes.model.Expense
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.repository.ApiRepository
import kotlinx.coroutines.launch

class TripDetailViewModel : ViewModel() {

    var viaje by mutableStateOf<Trip?>(null)
        private set
    val gastos = mutableStateListOf<Expense>()
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun cargar(tripId: Int) {
        viewModelScope.launch {
            cargando = true
            error = null
            runCatching { ApiRepository.detalleViaje(tripId) }
                .onSuccess { (trip, expenses) ->
                    viaje = trip
                    gastos.clear()
                    gastos.addAll(expenses)
                }
                .onFailure {
                    error = "No se pudo cargar el detalle: ${it.message}"
                }
            cargando = false
        }
    }

    fun totalCalculado(): Double = ExpenseCalculator.total(gastos)
}
