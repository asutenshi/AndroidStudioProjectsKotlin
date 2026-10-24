package com.example.guessthenumber

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GuessActivity : AppCompatActivity() {
    private var low = 0
    private var high = 0

    private lateinit var questionText: TextView
    private lateinit var answerButtons: View
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_guess)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.guess)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        questionText = findViewById(R.id.questionText)
        answerButtons = findViewById(R.id.answerButtons)
        restartButton = findViewById(R.id.restartButton)

        low = savedInstanceState?.getInt(STATE_LOW) ?: intent.getIntExtra(EXTRA_MIN, 0)
        high = savedInstanceState?.getInt(STATE_HIGH) ?: intent.getIntExtra(EXTRA_MAX, 0)

        findViewById<Button>(R.id.yesButton).setOnClickListener {
            low = middle() + 1
            render()
        }
        findViewById<Button>(R.id.noButton).setOnClickListener {
            high = middle()
            render()
        }
        restartButton.setOnClickListener { finish() }

        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_LOW, low)
        outState.putInt(STATE_HIGH, high)
    }

    private fun middle() = Math.floorDiv(low + high, 2)

    private fun render() {
        if (low == high) {
            questionText.text = getString(R.string.result, low)
            answerButtons.visibility = View.GONE
            restartButton.visibility = View.VISIBLE
        } else {
            questionText.text = getString(R.string.question, middle())
        }
    }

    companion object {
        const val EXTRA_MIN = "min"
        const val EXTRA_MAX = "max"
        private const val STATE_LOW = "low"
        private const val STATE_HIGH = "high"
    }
}
