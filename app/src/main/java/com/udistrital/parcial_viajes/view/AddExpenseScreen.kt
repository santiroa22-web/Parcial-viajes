package com.udistrital.parcial_viajes.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.udistrital.parcial_viajes.model.Trip
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiLavenderSoft
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted
import com.udistrital.parcial_viajes.viewmodel.AddExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    trip: Trip,
    viewModel: AddExpenseViewModel,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (viewModel.categorias.isEmpty()) viewModel.cargarCategorias()
    }

    val msInicio = remember(trip.startDate) { DateUtils.isoAMillis(trip.startDate) }
    val msFin = remember(trip.endDate) { DateUtils.isoAMillis(trip.endDate) }

    var mostrarFecha by remember { mutableStateOf(false) }
    val estadoFecha = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val despuesInicio = msInicio == null || utcTimeMillis >= msInicio
                val antesFin = msFin == null || utcTimeMillis <= msFin
                return despuesInicio && antesFin
            }
        }
    )

    var expanded by remember { mutableStateOf(false) }

    if (mostrarFecha) {
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoFecha.selectedDateMillis?.let { ms ->
                        viewModel.fecha = DateUtils.millisAIso(ms)
                        viewModel.mensajeError = null
                    }
                    mostrarFecha = false
                }) { Text("Aceptar", color = LumiLavender, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar", color = LumiTextMuted) } }
        ) { DatePicker(state = estadoFecha) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar gasto") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.limpiar()
                        onBackClick()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LumiLavender,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(color = LumiLavenderSoft, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Viaje: ${trip.name}\nPeríodo: ${DateUtils.isoABonito(trip.startDate)} - ${DateUtils.isoABonito(trip.endDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(12.dp)
                )
            }

            OutlinedTextField(
                value = viewModel.descripcion,
                onValueChange = {
                    viewModel.descripcion = it
                    viewModel.mensajeError = null
                },
                label = { Text("Descripción") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.monto,
                onValueChange = {
                    viewModel.monto = it
                    viewModel.mensajeError = null
                },
                label = { Text("Valor") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = viewModel.categoriaSeleccionada?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (viewModel.cargandoCategorias) {
                        DropdownMenuItem(text = { Text("Cargando...") }, onClick = {})
                    }
                    viewModel.categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                viewModel.categoriaSeleccionada = cat
                                viewModel.mensajeError = null
                                expanded = false
                            }
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = DateUtils.isoABonito(viewModel.fecha),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha del gasto") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = LumiLavender) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().clickable { mostrarFecha = true })
            }

            viewModel.mensajeError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = { viewModel.guardar(trip) { onSaved() } },
                enabled = !viewModel.guardando,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LumiLavender)
            ) {
                if (viewModel.guardando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}
