package com.udistrital.parcial_viajes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.udistrital.parcial_viajes.model.AppRoute
import com.udistrital.parcial_viajes.ui.theme.Parcial_viajesTheme
import com.udistrital.parcial_viajes.view.AddExpenseScreen
import com.udistrital.parcial_viajes.view.CreateTripScreen
import com.udistrital.parcial_viajes.view.HomeScreen
import com.udistrital.parcial_viajes.view.TripDetailScreen
import com.udistrital.parcial_viajes.viewmodel.AddExpenseViewModel
import com.udistrital.parcial_viajes.viewmodel.CreateTripViewModel
import com.udistrital.parcial_viajes.viewmodel.HomeViewModel
import com.udistrital.parcial_viajes.viewmodel.TripDetailViewModel
import com.udistrital.parcial_viajes.viewmodel.TripsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parcial_viajesTheme {
                val homeViewModel: HomeViewModel = viewModel()
                val tripsViewModel: TripsViewModel = viewModel()
                val createTripViewModel: CreateTripViewModel = viewModel()
                val tripDetailViewModel: TripDetailViewModel = viewModel()
                val addExpenseViewModel: AddExpenseViewModel = viewModel()

                val uiState by homeViewModel.uiState.collectAsState()

                BackHandler(enabled = uiState.currentRoute != AppRoute.HOME) {
                    val destino = when (uiState.currentRoute) {
                        AppRoute.ADD_EXPENSE -> AppRoute.TRIP_DETAIL
                        else -> AppRoute.HOME
                    }
                    homeViewModel.navigateTo(destino)
                }

                when (uiState.currentRoute) {
                    AppRoute.HOME -> HomeScreen(
                        viewModel = tripsViewModel,
                        onCreateTripClick = { homeViewModel.navigateTo(AppRoute.CREATE_TRIP) },
                        onTripClick = { trip ->
                            tripDetailViewModel.cargar(trip.id)
                            homeViewModel.navigateTo(AppRoute.TRIP_DETAIL, trip.id)
                        }
                    )

                    AppRoute.CREATE_TRIP -> CreateTripScreen(
                        viewModel = createTripViewModel,
                        onBackClick = { homeViewModel.navigateTo(AppRoute.HOME) },
                        onSaved = {
                            tripsViewModel.cargar()
                            homeViewModel.navigateTo(AppRoute.HOME)
                        }
                    )

                    AppRoute.TRIP_DETAIL -> TripDetailScreen(
                        viewModel = tripDetailViewModel,
                        onBackClick = { homeViewModel.navigateTo(AppRoute.HOME) },
                        onAddExpenseClick = { homeViewModel.navigateTo(AppRoute.ADD_EXPENSE) }
                    )

                    AppRoute.ADD_EXPENSE -> {
                        val viaje = tripDetailViewModel.viaje
                        if (viaje != null) {
                            AddExpenseScreen(
                                trip = viaje,
                                viewModel = addExpenseViewModel,
                                onBackClick = { homeViewModel.navigateTo(AppRoute.TRIP_DETAIL) },
                                onSaved = {
                                    tripDetailViewModel.cargar(viaje.id)
                                    tripsViewModel.cargar()
                                    homeViewModel.navigateTo(AppRoute.TRIP_DETAIL)
                                }
                            )
                        } else {
                            LaunchedEffect(Unit) {
                                homeViewModel.navigateTo(AppRoute.HOME)
                            }
                        }
                    }
                }
            }
        }
    }
}