package com.example.mazeexplorer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mazeexplorer.mazepieces.GoalPiece
import kotlinx.android.synthetic.main.activity_maze.*

class Maze : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        ReturnButton.setOnClickListener {
            val intent = Intent(this@Maze, MainActivity::class.java)
            startActivity(intent)
        }

        val goal = GoalPiece(this)

        constraintLayout.addView(goal.getImageView())
    }
}
