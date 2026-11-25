package com.udistrital.parcial_viajes.viewmodel

import androidx.lifecycle.ViewModel
import com.udistrital.parcial_viajes.model.AppRoute
import com.udistrital.parcial_viajes.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun navigateTo(route: AppRoute, tripId: Int? = null) {
        _uiState.update { state ->
            state.copy(
                currentRoute = route,
                selectedTripId = tripId ?: state.selectedTripId
            )
        }
    }
}
