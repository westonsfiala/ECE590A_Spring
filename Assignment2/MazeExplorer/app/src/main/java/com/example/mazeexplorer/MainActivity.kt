package com.example.mazeexplorer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DisplayText.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                DisplayText.text = "Left"
            }

            override fun onSwipeRight() {
                DisplayText.text = "Right"
            }

            override fun onSwipeTop() {
                DisplayText.text = "Top"
            }

            override fun onSwipeBottom() {
                DisplayText.text = "Bottom"
            }
        })

        StartMazeButton.setOnClickListener {
            val intent = Intent(this@MainActivity, Maze::class.java)
            startActivity(intent)
        }
    }
}
