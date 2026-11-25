package com.udistrital.parcial_viajes.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udistrital.parcial_viajes.domain.TripValidator
import com.udistrital.parcial_viajes.model.NewTripInput
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.repository.ApiRepository
import kotlinx.coroutines.launch

class CreateTripViewModel : ViewModel() {

    var nombre by mutableStateOf("")
    var destino by mutableStateOf("")
    var fechaInicio by mutableStateOf("")
    var fechaFin by mutableStateOf("")
    var mensajeError by mutableStateOf<String?>(null)
    var guardando by mutableStateOf(false)
        private set

    fun guardar(onExito: (Trip) -> Unit) {
        val input = NewTripInput(nombre.trim(), destino.trim(), fechaInicio, fechaFin)
        val error = TripValidator.validate(input)
        if (error != null) {
            mensajeError = error
            return
        }
        viewModelScope.launch {
            guardando = true
            mensajeError = null
            runCatching { ApiRepository.crearViaje(input) }
                .onSuccess { viaje ->
                    limpiar()
                    onExito(viaje)
                }
                .onFailure {
                    mensajeError = it.message ?: "No se pudo guardar"
                }
            guardando = false
        }
    }

    fun limpiar() {
        nombre = ""
        destino = ""
        fechaInicio = ""
        fechaFin = ""
        mensajeError = null
    }
}
