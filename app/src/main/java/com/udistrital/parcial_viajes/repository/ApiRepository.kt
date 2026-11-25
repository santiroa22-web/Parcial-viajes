package com.udistrital.parcial_viajes.repository

import com.udistrital.parcial_viajes.model.Category
import com.udistrital.parcial_viajes.model.Expense
import com.udistrital.parcial_viajes.model.NewExpenseInput
import com.udistrital.parcial_viajes.model.NewTripInput
import com.udistrital.parcial_viajes.model.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object ApiRepository {

    const val BASE_URL = "http://10.0.2.2:3000"
    private const val TIMEOUT_MS = 8000

    suspend fun listarViajes(): List<Trip> = withContext(Dispatchers.IO) {
        val arr = JSONArray(getText("$BASE_URL/api/trips"))
        List(arr.length()) { i -> parseTrip(arr.getJSONObject(i)) }
    }

    suspend fun detalleViaje(tripId: Int): Pair<Trip, List<Expense>> = withContext(Dispatchers.IO) {
        val o = JSONObject(getText("$BASE_URL/api/trips/$tripId"))
        val trip = parseTrip(o)
        val arr = o.optJSONArray("expenses") ?: JSONArray()
        val expenses = List(arr.length()) { i -> parseExpense(arr.getJSONObject(i), tripId) }
        trip to expenses
    }

    suspend fun crearViaje(input: NewTripInput): Trip = withContext(Dispatchers.IO) {
        val body = JSONObject()
            .put("name", input.name)
            .put("destination", input.destination)
            .put("startDate", input.startDate)
            .put("endDate", input.endDate)
        val o = JSONObject(postText("$BASE_URL/api/trips", body))
        parseTrip(o)
    }

    suspend fun listarGastos(tripId: Int): List<Expense> = withContext(Dispatchers.IO) {
        val arr = JSONArray(getText("$BASE_URL/api/expenses?tripId=$tripId"))
        List(arr.length()) { i -> parseExpense(arr.getJSONObject(i), tripId) }
    }

    suspend fun registrarGasto(input: NewExpenseInput): Expense = withContext(Dispatchers.IO) {
        val body = JSONObject()
            .put("tripId", input.tripId)
            .put("description", input.description)
            .put("amount", input.amount)
            .put("categoryId", input.categoryId)
            .put("expenseDate", input.expenseDate)
        val o = JSONObject(postText("$BASE_URL/api/expenses", body))
        parseExpense(o, input.tripId)
    }

    suspend fun listarCategorias(): List<Category> = withContext(Dispatchers.IO) {
        val arr = JSONArray(getText("$BASE_URL/api/categories"))
        List(arr.length()) { i ->
            val o = arr.getJSONObject(i)
            Category(o.getInt("id"), o.getString("name"), o.optString("description", ""))
        }
    }

    private fun parseTrip(o: JSONObject) = Trip(
        id = o.getInt("id"),
        name = o.getString("name"),
        destination = o.getString("destination"),
        startDate = o.getString("startDate"),
        endDate = o.getString("endDate"),
        totalExpenses = o.optDouble("totalExpenses", 0.0),
        expenseCount = o.optInt("expenseCount", 0)
    )

    private fun parseExpense(o: JSONObject, fallbackTripId: Int) = Expense(
        id = o.getInt("id"),
        tripId = o.optInt("tripId", fallbackTripId),
        description = o.getString("description"),
        amount = o.getDouble("amount"),
        expenseDate = o.getString("expenseDate"),
        categoryId = o.getInt("categoryId"),
        categoryName = o.getString("categoryName")
    )

    private fun getText(url: String): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = TIMEOUT_MS
            conn.readTimeout = TIMEOUT_MS
            conn.setRequestProperty("Accept", "application/json")
            return readResponse(conn)
        } finally {
            conn.disconnect()
        }
    }

    private fun postText(url: String, body: JSONObject): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "POST"
            conn.connectTimeout = TIMEOUT_MS
            conn.readTimeout = TIMEOUT_MS
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.outputStream.use { it.write(body.toString().toByteArray()) }
            return readResponse(conn)
        } finally {
            conn.disconnect()
        }
    }

    private fun readResponse(conn: HttpURLConnection): String {
        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val text = stream?.bufferedReader()?.use { it.readText() } ?: "{}"
        if (code !in 200..299) {
            val message = try { JSONObject(text).optString("message", "Error del servidor") }
                catch (e: Exception) { "Error del servidor ($code)" }
            throw Exception(message)
        }
        return text
    }
}
