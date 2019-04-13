package edu.uw.pmpee590.swipeanimation

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Gravity
import android.R.attr.gravity
import android.widget.LinearLayout



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        swipe_text.setOnTouchListener(object : OnSwipeListener(this) {
                init {
                    setDragHorizontal(true)
                    setExitScreenOnSwipe(true)
                    setAnimationDelay(500)
                }

                override fun onSwipeLeft(distance: Float) {
                    Toast.makeText(applicationContext, "swiped left!", Toast.LENGTH_SHORT).show()
                    knote_bitmap.setImageResource(R.drawable.butterfly)
                }

            override fun onSwipeRight(distance: Float) {
                Toast.makeText(applicationContext, "swiped right!", Toast.LENGTH_SHORT).show()
                knote_bitmap.setImageResource(R.drawable.figure8)

                }
            }
        )
    }

}
