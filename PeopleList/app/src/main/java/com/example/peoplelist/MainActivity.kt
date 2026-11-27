package com.example.peoplelist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    val people = mutableListOf<String>()
    lateinit var adapter: ArrayAdapter<String>
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lvPeople = findViewById<ListView>(R.id.people)
        val names = resources.getStringArray(R.array.names)
        val surnames = resources.getStringArray(R.array.surnames)

        repeat(25) {
            people.add("${names.random()} ${surnames.random()}")
        }

        adapter = object : ArrayAdapter<String>(this, R.layout.item, people) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val colorRes = if (position == selectedPosition) {
                    R.color.item_selected
                } else {
                    R.color.item
                }
                view.setBackgroundColor(getColor(colorRes))
                return view
            }
        }

        lvPeople.adapter = adapter

        lvPeople.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            adapter.notifyDataSetChanged()
        }
    }

    fun onAddPersonClick(view: View) {
        val etName = findViewById<EditText>(R.id.etName)
        people.add(etName.text.toString())
        adapter.notifyDataSetChanged()
        etName.text.clear()
    }
}
