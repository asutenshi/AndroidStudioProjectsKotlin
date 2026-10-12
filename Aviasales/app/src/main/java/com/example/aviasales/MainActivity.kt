package com.example.aviasales

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etDateFrom = findViewById<EditText>(R.id.etDateFrom)
        val etDateTo = findViewById<EditText>(R.id.etDateTo)

        etDateFrom.setOnClickListener { pickDate(etDateFrom) }
        etDateTo.setOnClickListener { pickDate(etDateTo) }
    }

    private fun pickDate(target: EditText) {
        val c = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                target.setText(String.format("%02d.%02d.%04d", day, month + 1, year))
            },
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }
}