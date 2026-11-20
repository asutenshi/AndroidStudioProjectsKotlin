package com.example.memorina

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
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

    private var firstCard: ImageView? = null
    private var openCardsCount = 0
    private var cardsLeft = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val images = (faceImages + faceImages).shuffled()
        cardsLeft = images.size

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
                setImageResource(backImage)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                setPadding(padding, padding, padding, padding)
                tag = image
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

    private suspend fun checkPair(first: ImageView, second: ImageView) {
        delay(1000)
        if (first.tag == second.tag) {
            first.visibility = View.INVISIBLE
            second.visibility = View.INVISIBLE
            cardsLeft -= 2
        } else {
            first.setImageResource(backImage)
            second.setImageResource(backImage)
            first.isClickable = true
            second.isClickable = true
        }
        firstCard = null
        openCardsCount = 0

        if (cardsLeft == 0) showWin()
    }

    private fun showWin() {
        AlertDialog.Builder(this)
            .setTitle(R.string.win_title)
            .setMessage(R.string.win_message)
            .setCancelable(false)
            .setPositiveButton(R.string.play_again) { _, _ -> recreate() }
            .show()
    }

    private val colorListener = View.OnClickListener { v ->
        val card = v as ImageView
        when (openCardsCount) {
            0 -> {
                card.setImageResource(card.tag as Int)
                card.isClickable = false
                firstCard = card
                openCardsCount = 1
            }
            1 -> {
                card.setImageResource(card.tag as Int)
                card.isClickable = false
                openCardsCount = 2
                lifecycleScope.launch { checkPair(firstCard!!, card) }
            }
        }
    }
}
