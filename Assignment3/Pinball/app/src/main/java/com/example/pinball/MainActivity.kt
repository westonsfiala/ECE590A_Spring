package com.example.pinball

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ball = BallView(this)

        background.addView(ball)

        background.setOnTouchListener { _, motionEvent ->
            ball.x = motionEvent.x - ball.width/2
            ball.y = motionEvent.y - ball.height/2
            true
        }
    }

    inner class BallView(context: Context) : ImageView(context) {
        init {
            setImageResource(R.drawable.ball)
        }
    }
}
