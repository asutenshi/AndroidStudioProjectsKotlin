package com.example.sumtwonumbers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var numberA: EditText
    lateinit var numberB: EditText
    lateinit var sumResult: TextView
    lateinit var buttonSum: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberA = findViewById<EditText>(R.id.numberA)
        numberB = findViewById<EditText>(R.id.numberB)
        sumResult = findViewById<TextView>(R.id.sumResult)
    }

    fun onClick(view: View) {
        val a = numberA.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
        val b = numberB.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
        val sum = a + b
        sumResult.text = sum.toString()
    }
}