package com.example.mazeexplorer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import com.example.mazeexplorer.mazepieces.MazeMap
import com.example.mazeexplorer.mazepieces.Player
import kotlinx.android.synthetic.main.activity_maze.*

class Maze : AppCompatActivity() {

    private lateinit var map: MazeMap

    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        ReturnButton.setOnClickListener {
            val intent = Intent(this@Maze, MainActivity::class.java)
            startActivity(intent)
        }

        background.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                map.movePlayerLeft()
                movePlayer()
            }

            override fun onSwipeRight() {
                map.movePlayerRight()
                movePlayer()
            }

            override fun onSwipeTop() {
                map.movePlayerUp()
                movePlayer()
            }

            override fun onSwipeBottom() {
                map.movePlayerDown()
                movePlayer()
            }
        })

        val size = intent.getIntExtra("size", 5)

        setupMaze(size)
        setupPlayer()
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

    private fun setupPlayer() {
        player = Player(this)
        player.id = "Player".hashCode()

        background.addView(player)

        // When the map is ready, perform the move player action.
        map.post {movePlayer()}
    }

    private fun movePlayer() {

        val location = map.getCurrentPieceLocationCenter()

        // Adjust the player location to be centered in the tile
        player.x = location[0] - player.width / 2
        player.y = location[1] - player.height / 2
    }
}
