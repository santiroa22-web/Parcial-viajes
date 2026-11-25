package com.udistrital.parcial_viajes.view

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private val iso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val bonito = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun millisAIso(millis: Long): String = iso.format(Date(millis))

    fun isoAMillis(fecha: String): Long? = try {
        iso.parse(fecha)?.time
    } catch (e: Exception) {
        null
    }

    fun isoABonito(fecha: String): String {
        if (fecha.isBlank()) return ""
        return try {
            bonito.format(iso.parse(fecha) ?: return fecha)
        } catch (e: Exception) {
            fecha
        }
    }
}
