package com.udistrital.parcial_viajes.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiLavenderSoft
import com.udistrital.parcial_viajes.ui.theme.LumiSurface
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted
import com.udistrital.parcial_viajes.viewmodel.TripsViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TripsViewModel,
    onCreateTripClick: () -> Unit,
    onTripClick: (Trip) -> Unit
) {
    val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis viajes", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LumiLavender,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTripClick,
                containerColor = LumiLavender,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear viaje")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (viewModel.cargando) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = LumiLavender)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cargando...", color = LumiTextMuted)
                }
            }

            viewModel.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = { viewModel.cargar() }) {
                    Text("Reintentar", color = LumiLavender)
                }
            }

            if (!viewModel.cargando && viewModel.trips.isEmpty() && viewModel.error == null) {
                Text("Aún no tienes viajes registrados.", color = LumiTextMuted)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(viewModel.trips) { trip ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTripClick(trip) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = LumiSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = trip.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = trip.destination,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LumiTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${DateUtils.isoABonito(trip.startDate)} - ${DateUtils.isoABonito(trip.endDate)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = LumiTextMuted
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = LumiLavenderSoft,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Total: ${formato.format(trip.totalExpenses)}  •  ${trip.expenseCount} gastos",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
