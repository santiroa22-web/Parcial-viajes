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
import com.udistrital.parcial_viajes.model.ExpenseUiItem
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiLavenderSoft
import com.udistrital.parcial_viajes.ui.theme.LumiMint
import com.udistrital.parcial_viajes.ui.theme.LumiSurface
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    tripName: String,
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    val expenses = listOf(
        ExpenseUiItem("Alimentación", "Cena en Trastevere", "12/06/2026", "$ 28.50"),
        ExpenseUiItem("Alojamiento", "Hotel centro", "10/06/2026", "$ 260.00"),
        ExpenseUiItem("Transporte", "Metro", "13/06/2026", "$ 7.00")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tripName) },
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
            FloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = LumiLavender,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Registrar gasto")
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = LumiLavenderSoft)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(tripName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Destino: Italia")
                    Text("Período: 10/06/2026 - 20/06/2026")
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
                        text = "$ 295.50",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Gastos registrados (${expenses.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
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
                                Text(expense.category, fontWeight = FontWeight.SemiBold)
                                Text(expense.description, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = expense.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LumiTextMuted
                                )
                            }
                            Text(
                                text = expense.amount,
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