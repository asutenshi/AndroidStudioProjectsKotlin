package com.example.weatherapp

import org.json.JSONObject
import java.util.Locale

data class WeatherData(
    val city: String,
    val temp: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val clouds: Int,
) {
    val windRotation: Float get() = windDeg + 180f

    val cloudAlpha: Float get() = 0.15f + clouds * 0.0085f

    val compassIndex: Int get() = ((windDeg % 360 + 360) % 360 + 22) / 45 % 8

    fun tempText(fahrenheit: Boolean): String = formatTemp(temp, fahrenheit)

    fun feelsLikeText(fahrenheit: Boolean): String = formatTemp(feelsLike, fahrenheit)

    companion object {
        fun celsiusToFahrenheit(c: Double): Double = c * 9.0 / 5.0 + 32.0

        fun formatTemp(celsius: Double, fahrenheit: Boolean): String =
            if (fahrenheit) String.format(Locale.US, "%.1f°F", celsiusToFahrenheit(celsius))
            else String.format(Locale.US, "%.1f°C", celsius)

        fun fromJson(json: String): WeatherData {
            val o = JSONObject(json)
            val main = o.getJSONObject("main")
            val wind = o.optJSONObject("wind")
            val weather = o.getJSONArray("weather").getJSONObject(0)
            return WeatherData(
                city = o.getString("name"),
                temp = main.getDouble("temp"),
                feelsLike = main.getDouble("feels_like"),
                description = weather.getString("description").replaceFirstChar { it.uppercase() },
                humidity = main.getInt("humidity"),
                pressure = main.getInt("pressure"),
                windSpeed = wind?.optDouble("speed", 0.0) ?: 0.0,
                windDeg = wind?.optInt("deg", 0) ?: 0,
                clouds = o.optJSONObject("clouds")?.optInt("all", 0) ?: 0,
            )
        }
    }
}
