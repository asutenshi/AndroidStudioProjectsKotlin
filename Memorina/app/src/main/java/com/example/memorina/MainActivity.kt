package com.example.memorina

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val backImage = R.drawable.card_back

    private val faceImages = listOf(
        R.drawable.face_circle,
        R.drawable.face_square,
        R.drawable.face_triangle,
        R.drawable.face_diamond,
        R.drawable.face_star,
        R.drawable.face_plus,
        R.drawable.face_heart,
        R.drawable.face_hexagon
    )

    private val columns = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val images = (faceImages + faceImages).shuffled()

        val padding = (6 * resources.displayMetrics.density).toInt()

        val rows = Array(images.size / columns) {
            LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
                )
            }
        }

        for ((index, image) in images.withIndex()) {
            val card = ImageView(this).apply {
                setImageResource(backImage) // изначально рубашка
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                setPadding(padding, padding, padding, padding)
                tag = image // в теге хранится картинка лицевой стороны
                setOnClickListener(colorListener)
            }
            rows[index / columns].addView(card)
        }
        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
        ViewCompat.setOnApplyWindowInsetsListener(layout) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    private suspend fun flipWithDelay(card: ImageView) {
        card.isClickable = false
        card.setImageResource(card.tag as Int)
        delay(1000)
        card.setImageResource(backImage)
        card.isClickable = true
    }

    private val colorListener = View.OnClickListener { v ->
        lifecycleScope.launch { flipWithDelay(v as ImageView) }
    }
}
