package com.udistrital.parcial_viajes.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiLavenderSoft
import com.udistrital.parcial_viajes.ui.theme.LumiMint
import com.udistrital.parcial_viajes.ui.theme.LumiSurface
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted
import com.udistrital.parcial_viajes.viewmodel.TripDetailViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    viewModel: TripDetailViewModel,
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val formato = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
        maximumFractionDigits = 0
    }
    val viaje = viewModel.viaje

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viaje?.name ?: "Detalle del viaje") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LumiLavender,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (viaje != null) {
                FloatingActionButton(
                    onClick = onAddExpenseClick,
                    containerColor = LumiLavender,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Registrar gasto")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (viewModel.cargando && viaje == null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = LumiLavender)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cargando...", color = LumiTextMuted)
                }
            }

            viewModel.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (viaje != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = LumiLavenderSoft)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(viaje.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Destino: ${viaje.destination}")
                        Text("Período: ${DateUtils.isoABonito(viaje.startDate)} - ${DateUtils.isoABonito(viaje.endDate)}")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = LumiMint)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total de gastos", style = MaterialTheme.typography.labelLarge)
                        Text(
                            text = formato.format(viewModel.totalCalculado()),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("${viewModel.gastos.size} gastos registrados", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Text(
                    text = "Gastos registrados (${viewModel.gastos.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (viewModel.gastos.isEmpty() && !viewModel.cargando) {
                    Text("No hay gastos aún.", color = LumiTextMuted)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.gastos) { expense ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = LumiSurface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(expense.categoryName, fontWeight = FontWeight.SemiBold)
                                    Text(expense.description, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        text = DateUtils.isoABonito(expense.expenseDate),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LumiTextMuted
                                    )
                                }
                                Text(
                                    text = formato.format(expense.amount),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(LumiLavenderSoft, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
