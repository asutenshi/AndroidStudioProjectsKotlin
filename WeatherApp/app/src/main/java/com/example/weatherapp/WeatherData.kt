package com.example.weatherapp

import org.json.JSONObject

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

    companion object {
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
