package com.example.weatherapp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        binding.citySpinner.adapter = ArrayAdapter.createFromResource(
            this, R.array.cities, android.R.layout.simple_spinner_dropdown_item
        )

        binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                loadWeather()
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
        binding.refreshButton.setOnClickListener { loadWeather() }
    }

    private fun loadWeather() {
        val city = binding.citySpinner.selectedItem as? String ?: return
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            binding.loading = true
            binding.error = null
            try {
                val data = withContext(Dispatchers.IO) {
                    WeatherApi.load(city, BuildConfig.OPENWEATHER_API_KEY)
                }
                binding.weather = data
            } catch (e: WeatherException) {
                binding.weather = null
                binding.error = errorText(e)
            }
            binding.loading = false
        }
    }

    private fun errorText(e: WeatherException): String = when (e.kind) {
        WeatherException.Kind.NO_NETWORK -> getString(R.string.error_no_network)
        WeatherException.Kind.BAD_KEY -> getString(R.string.error_bad_key)
        WeatherException.Kind.CITY_NOT_FOUND -> getString(R.string.error_city_not_found)
        WeatherException.Kind.EMPTY -> getString(R.string.error_empty)
        WeatherException.Kind.HTTP -> getString(R.string.error_generic, e.httpCode)
    }
}
