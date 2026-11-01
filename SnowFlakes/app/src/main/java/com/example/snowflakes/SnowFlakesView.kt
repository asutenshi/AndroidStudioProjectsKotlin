package com.example.snowflakes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin
import kotlin.random.Random

class Flake(
    val startX: Float,
    var x: Float,
    var y: Float,
    val radius: Float,
    val color: Int,
    var phase: Float
)

class SnowFlakesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val flakes = ArrayList<Flake>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var task: MoveTask? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        flakes.clear()
        repeat(80) {
            val x = Random.nextFloat() * w
            flakes.add(
                Flake(
                    startX = x,
                    x = x,
                    y = Random.nextFloat() * h,
                    radius = Random.nextFloat() * 8f + 4f,
                    color = Color.rgb(
                        Random.nextInt(150, 256),
                        Random.nextInt(200, 256),
                        255
                    ),
                    phase = Random.nextFloat() * 6.28f
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (f in flakes) {
            paint.color = f.color
            canvas.drawCircle(f.x, f.y, f.radius, paint)
        }
    }

    private fun moveFlakes() {
        val h = height.toFloat()
        for (f in flakes) {
            val speed = 1f + 6f * (1f - f.y / h)
            f.y += speed

            f.phase += 0.05f
            f.x = f.startX + sin(f.phase) * 30f

            if (f.y > h) {
                f.y = 0f
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        task = MoveTask().also { it.execute() }
    }

    override fun onDetachedFromWindow() {
        task?.cancel(true)
        task = null
        super.onDetachedFromWindow()
    }

    private inner class MoveTask : AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            while (!isCancelled) {
                moveFlakes()
                publishProgress()
                try {
                    Thread.sleep(16)
                } catch (e: InterruptedException) {
                    break
                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Void?) {
            invalidate()
        }
    }
}
