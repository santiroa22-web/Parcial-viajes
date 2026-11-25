package com.udistrital.parcial_viajes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.repository.ApiRepository
import kotlinx.coroutines.launch

class TripsViewModel : ViewModel() {

    val trips = mutableStateListOf<Trip>()
    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            cargando = true
            error = null
            runCatching { ApiRepository.listarViajes() }
                .onSuccess {
                    trips.clear()
                    trips.addAll(it)
                }
                .onFailure {
                    error = "No se pudieron cargar los viajes: ${it.message}"
                }
            cargando = false
        }
    }
}
