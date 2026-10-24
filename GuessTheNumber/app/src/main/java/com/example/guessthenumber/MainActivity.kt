package com.example.guessthenumber

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val minInput = findViewById<EditText>(R.id.minInput)
        val maxInput = findViewById<EditText>(R.id.maxInput)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            val min = minInput.text.toString().toIntOrNull()
            val max = maxInput.text.toString().toIntOrNull()
            if (min == null || max == null || min >= max) {
                Toast.makeText(this, R.string.range_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(
                Intent(this, GuessActivity::class.java)
                    .putExtra(GuessActivity.EXTRA_MIN, min)
                    .putExtra(GuessActivity.EXTRA_MAX, max)
            )
        }
    }
}
