package com.udistrital.parcial_viajes.domain

import com.udistrital.parcial_viajes.model.NewExpenseInput
import com.udistrital.parcial_viajes.model.NewTripInput

object TripValidator {

    fun validate(input: NewTripInput): String? {
        if (input.name.isBlank()) return "El nombre es obligatorio"
        if (input.destination.isBlank()) return "El destino es obligatorio"
        if (input.startDate.isBlank()) return "La fecha de inicio es obligatoria"
        if (input.endDate.isBlank()) return "La fecha de finalización es obligatoria"
        if (input.endDate < input.startDate) return "La fecha de finalización no puede ser anterior a la de inicio"
        return null
    }
}

object ExpenseValidator {

    fun validate(input: NewExpenseInput, tripStartDate: String, tripEndDate: String): String? {
        if (input.description.isBlank()) return "La descripción es obligatoria"
        if (input.amount <= 0.0) return "El valor debe ser mayor que cero"
        if (input.categoryId <= 0) return "Selecciona una categoría"
        if (input.expenseDate.isBlank()) return "La fecha del gasto es obligatoria"
        if (input.expenseDate < tripStartDate) return "La fecha no puede ser anterior al inicio del viaje"
        if (input.expenseDate > tripEndDate) return "La fecha no puede ser posterior al fin del viaje"
        return null
    }
}
