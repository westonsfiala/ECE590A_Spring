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

        setupMaze()
    }

    private fun setupMaze()
    {
        map = MazeMap(this,10,15)
        map.id = "MazeMap".hashCode()

        constraintLayout.addView(map)

        val set = ConstraintSet()
        set.clone(constraintLayout)

        set.constrainWidth(map.id, ConstraintSet.WRAP_CONTENT)
        set.centerVertically(map.id, ConstraintSet.PARENT_ID)
        set.centerHorizontally(map.id, ConstraintSet.PARENT_ID)

        set.applyTo(constraintLayout)
    }
}
