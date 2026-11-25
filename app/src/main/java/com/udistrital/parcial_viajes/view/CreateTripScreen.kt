package com.udistrital.parcial_viajes.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.udistrital.parcial_viajes.ui.theme.LumiLavender
import com.udistrital.parcial_viajes.ui.theme.LumiTextMuted
import com.udistrital.parcial_viajes.viewmodel.CreateTripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    viewModel: CreateTripViewModel,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    var mostrarInicio by remember { mutableStateOf(false) }
    var mostrarFin by remember { mutableStateOf(false) }
    val estadoInicio = rememberDatePickerState()
    val estadoFin = rememberDatePickerState()

    if (mostrarInicio) {
        DatePickerDialog(
            onDismissRequest = { mostrarInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoInicio.selectedDateMillis?.let { ms ->
                        viewModel.fechaInicio = DateUtils.millisAIso(ms)
                        viewModel.mensajeError = null
                    }
                    mostrarInicio = false
                }) { Text("Aceptar", color = LumiLavender, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { mostrarInicio = false }) { Text("Cancelar", color = LumiTextMuted) } }
        ) { DatePicker(state = estadoInicio) }
    }

    if (mostrarFin) {
        DatePickerDialog(
            onDismissRequest = { mostrarFin = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoFin.selectedDateMillis?.let { ms ->
                        viewModel.fechaFin = DateUtils.millisAIso(ms)
                        viewModel.mensajeError = null
                    }
                    mostrarFin = false
                }) { Text("Aceptar", color = LumiLavender, fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { mostrarFin = false }) { Text("Cancelar", color = LumiTextMuted) } }
        ) { DatePicker(state = estadoFin) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear viaje") },
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
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = {
                    viewModel.nombre = it
                    viewModel.mensajeError = null
                },
                label = { Text("Nombre del viaje") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.destino,
                onValueChange = {
                    viewModel.destino = it
                    viewModel.mensajeError = null
                },
                label = { Text("Destino") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = DateUtils.isoABonito(viewModel.fechaInicio),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de inicio") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = LumiLavender) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().clickable { mostrarInicio = true })
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = DateUtils.isoABonito(viewModel.fechaFin),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de finalización") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = LumiLavender) },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().clickable { mostrarFin = true })
            }

            viewModel.mensajeError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
            }

            Button(
                onClick = { viewModel.guardar { onSaved() } },
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
