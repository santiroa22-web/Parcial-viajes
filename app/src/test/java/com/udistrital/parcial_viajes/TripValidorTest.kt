package com.udistrital.parcial_viajes

import com.udistrital.parcial_viajes.domain.TripValidator
import com.udistrital.parcial_viajes.model.NewTripInput
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TripValidatorTest {

    @Test
    fun viajeValidoRetornaNull() {
        val input = NewTripInput("Europa", "Italia", "2026-06-10", "2026-06-20")
        assertNull(TripValidator.validate(input))
    }

    @Test
    fun rechazaNombreVacio() {
        val input = NewTripInput("", "Italia", "2026-06-10", "2026-06-20")
        assertNotNull(TripValidator.validate(input))
    }

    @Test
    fun rechazaDestinoVacio() {
        val input = NewTripInput("Europa", "", "2026-06-10", "2026-06-20")
        assertNotNull(TripValidator.validate(input))
    }

    @Test
    fun rechazaFechaInicioVacia() {
        val input = NewTripInput("Europa", "Italia", "", "2026-06-20")
        assertNotNull(TripValidator.validate(input))
    }

    @Test
    fun rechazaFechaFinVacia() {
        val input = NewTripInput("Europa", "Italia", "2026-06-10", "")
        assertNotNull(TripValidator.validate(input))
    }

    @Test
    fun rechazaFechaFinAnteriorAInicio() {
        val input = NewTripInput("Europa", "Italia", "2026-06-20", "2026-06-10")
        assertNotNull(TripValidator.validate(input))
    }
}