package com.example.mazeexplorer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import com.example.mazeexplorer.mazepieces.MazeMap
import kotlinx.android.synthetic.main.activity_maze.*

class Maze : AppCompatActivity() {

    private lateinit var map: MazeMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        ReturnButton.setOnClickListener {
            val intent = Intent(this@Maze, MainActivity::class.java)
            startActivity(intent)
        }

        background.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                //DisplayText.text = "Left"
            }

            override fun onSwipeRight() {
               //DisplayText.text = "Right"
            }

            override fun onSwipeTop() {
                //DisplayText.text = "Top"
            }

            override fun onSwipeBottom() {
                map.start()
            }
        })

        val size = intent.getIntExtra("size", 5)

        setupMaze(size)
    }

    private fun setupMaze(size : Int)
    {
        map = MazeMap(this,size,size)
        map.id = "MazeMap".hashCode()

        background.addView(map)

        val set = ConstraintSet()
        set.clone(background)

        set.constrainWidth(map.id, ConstraintSet.WRAP_CONTENT)
        set.centerVertically(map.id, ConstraintSet.PARENT_ID)
        set.centerHorizontally(map.id, ConstraintSet.PARENT_ID)

        set.applyTo(background)
    }
}
