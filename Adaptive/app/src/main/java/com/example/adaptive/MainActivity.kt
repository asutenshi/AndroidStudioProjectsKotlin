package com.example.adaptive

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val pictures = intArrayOf(R.drawable.car1, R.drawable.car2, R.drawable.car3)

    private lateinit var prefs: SharedPreferences
    private lateinit var imageView: ImageView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        prefs = getSharedPreferences("settings", MODE_PRIVATE)
        imageView = findViewById(R.id.picture)
        spinner = findViewById(R.id.pictures_list)

        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.pictures, R.layout.item)

        val saved = prefs.getInt("picture", 0)
        spinner.setSelection(saved)
        spinner.onItemSelectedListener = this
        showPicture(saved)
    }

    fun onChangePictureClick(v: View) {
        val next = (spinner.selectedItemPosition + 1) % pictures.size
        spinner.setSelection(next) // вызовет onItemSelected
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        showPicture(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun showPicture(index: Int) {
        imageView.setImageResource(pictures[index])
        prefs.edit().putInt("picture", index).apply()
    }
}
