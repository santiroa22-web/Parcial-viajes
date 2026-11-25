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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.parcial_viajes.model.TripUiItem
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiSurface
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCreateTripClick: () -> Unit,
    onTripClick: (String) -> Unit
) {
    val sampleTrips = listOf(
        TripUiItem("Europa 2026", "Italia"),
        TripUiItem("Santa Marta", "Colombia"),
        TripUiItem("Bogotá Cultural", "Colombia")
    )

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(sampleTrips) { trip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTripClick(trip.name) },
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
                    }
                }
            }
        }
    }
}