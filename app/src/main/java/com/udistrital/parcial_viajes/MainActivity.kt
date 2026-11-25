package com.udistrital.parcial_viajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.udistrital.parcial_viajes.model.AppRoute
import com.udistrital.parcial_viajes.ui.theme.Parcial_viajesTheme
import com.udistrital.parcial_viajes.view.AddExpenseScreen
import com.udistrital.parcial_viajes.view.CreateTripScreen
import com.udistrital.parcial_viajes.view.HomeScreen
import com.udistrital.parcial_viajes.view.TripDetailScreen
import com.udistrital.parcial_viajes.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parcial_viajesTheme {
                val homeViewModel = remember { HomeViewModel() }
                val uiState by homeViewModel.uiState.collectAsState()

                when (uiState.currentRoute) {
                    AppRoute.HOME -> HomeScreen(
                        onCreateTripClick = { homeViewModel.navigateTo(AppRoute.CREATE_TRIP) },
                        onTripClick = { name -> homeViewModel.navigateTo(AppRoute.TRIP_DETAIL, name) }
                    )

                    AppRoute.CREATE_TRIP -> CreateTripScreen(
                        onBackClick = { homeViewModel.navigateTo(AppRoute.HOME) },
                        onSaveClick = { homeViewModel.navigateTo(AppRoute.HOME) }
                    )

                    AppRoute.TRIP_DETAIL -> TripDetailScreen(
                        tripName = uiState.selectedTripTitle,
                        onBackClick = { homeViewModel.navigateTo(AppRoute.HOME) },
                        onAddExpenseClick = { homeViewModel.navigateTo(AppRoute.ADD_EXPENSE) }
                    )

                    AppRoute.ADD_EXPENSE -> AddExpenseScreen(
                        onBackClick = { homeViewModel.navigateTo(AppRoute.TRIP_DETAIL) },
                        onSaveClick = { homeViewModel.navigateTo(AppRoute.TRIP_DETAIL) }
                    )
                }
            }
        }
    }
}