package com.example.pinball

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var corx = 0f
    var cory = 0f
    private val paint = Paint()

    private lateinit var drawing: CircleDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPaint()

        drawing = CircleDrawer(this)
        background.addView(drawing)
        drawing.setOnTouchListener { _, motionEvent ->
            corx = motionEvent.x
            cory = motionEvent.y
            drawing.performClick()
            true
        }

        Thread ( Runnable {
            fun run() {
                corx /= 2
                cory /= 2
                //drawing.performClick()
            }
        }).start()
    }

    private fun initPaint() {
        paint.setARGB(255, 255, 0, 0)
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
    }

    inner class CircleDrawer(context: Context) : View(context) {

        override fun onDraw(canvas: Canvas) {
            canvas.drawRGB(255, 255, 255)
            canvas.drawCircle(corx, cory, 20f, paint)
        }

        override fun performClick(): Boolean {
            invalidate()
            return super.performClick()
        }
    }
}
