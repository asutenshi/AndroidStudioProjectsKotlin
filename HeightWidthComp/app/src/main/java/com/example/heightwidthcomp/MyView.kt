package com.example.heightwidthcomp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("SIZE", "onLayout: width=${right - left}, height=${bottom - top}")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.CYAN)
        Log.d("SIZE", "onDraw: canvas.width=${canvas.width}, canvas.height=${canvas.height}")
        Log.d("SIZE", "onDraw: view.width=$width, view.height=$height")
        Log.d("SIZE", "density=${resources.displayMetrics.density}")
    }
}
