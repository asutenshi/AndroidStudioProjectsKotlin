package com.example.weatherapp

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object LocalWeather {
    fun load(context: Context, city: String): WeatherData? = try {
        val text = context.assets.open("weather.json").bufferedReader().use { it.readText() }
        JSONObject(text).optJSONObject(city)?.let { WeatherData.fromJson(it.toString()) }
    } catch (e: IOException) {
        null
    } catch (e: JSONException) {
        null
    }
}
