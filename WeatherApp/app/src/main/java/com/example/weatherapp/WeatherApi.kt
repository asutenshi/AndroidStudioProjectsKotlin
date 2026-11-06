package com.example.weatherapp

import org.json.JSONException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class WeatherException(val kind: Kind, val httpCode: Int = 0) : Exception() {
    enum class Kind { NO_NETWORK, BAD_KEY, CITY_NOT_FOUND, EMPTY, HTTP }
}

object WeatherApi {
    fun load(city: String, apiKey: String): WeatherData {
        val q = URLEncoder.encode(city, "UTF-8")
        val key = URLEncoder.encode(apiKey, "UTF-8")
        val url = URL(
            "https://api.openweathermap.org/data/2.5/weather?q=$q&appid=$key&units=metric&lang=ru"
        )
        val conn = url.openConnection() as HttpURLConnection
        try {
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            val body = when (val code = conn.responseCode) {
                HttpURLConnection.HTTP_OK -> conn.inputStream.bufferedReader().use { it.readText() }
                HttpURLConnection.HTTP_UNAUTHORIZED -> throw WeatherException(WeatherException.Kind.BAD_KEY)
                HttpURLConnection.HTTP_NOT_FOUND -> throw WeatherException(WeatherException.Kind.CITY_NOT_FOUND)
                else -> throw WeatherException(WeatherException.Kind.HTTP, code)
            }
            if (body.isBlank()) throw WeatherException(WeatherException.Kind.EMPTY)
            return WeatherData.fromJson(body)
        } catch (e: IOException) {
            throw WeatherException(WeatherException.Kind.NO_NETWORK)
        } catch (e: JSONException) {
            throw WeatherException(WeatherException.Kind.EMPTY)
        } finally {
            conn.disconnect()
        }
    }
}
