package com.example.randomfilm

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var films: Array<String>
    private lateinit var filmText: TextView

    // Индексы ещё не показанных фильмов
    private var remaining = mutableListOf<Int>()

    // Индекс показанного фильма, -1 — ничего не показано, -2 — фильмы закончились
    private var current = NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        films = resources.getStringArray(R.array.films)
        filmText = findViewById(R.id.filmText)

        if (savedInstanceState != null) {
            remaining = savedInstanceState.getIntegerArrayList(KEY_REMAINING)?.toMutableList()
                ?: films.indices.toMutableList()
            current = savedInstanceState.getInt(KEY_CURRENT, NONE)
        } else {
            remaining = films.indices.toMutableList()
        }
        render()

        findViewById<Button>(R.id.nextButton).setOnClickListener { showNext() }
        findViewById<Button>(R.id.resetButton).setOnClickListener { reset() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList(KEY_REMAINING, ArrayList(remaining))
        outState.putInt(KEY_CURRENT, current)
    }

    private fun showNext() {
        if (remaining.isEmpty()) {
            current = EMPTY
        } else {
            current = remaining.removeAt(remaining.indices.random())
        }
        render()
    }

    private fun reset() {
        remaining = films.indices.toMutableList()
        current = NONE
        render()
    }

    private fun render() {
        filmText.text = when (current) {
            NONE -> getString(R.string.hint)
            EMPTY -> getString(R.string.no_more_films)
            else -> films[current]
        }
    }

    private companion object {
        const val KEY_REMAINING = "remaining"
        const val KEY_CURRENT = "current"
        const val NONE = -1
        const val EMPTY = -2
    }
}
