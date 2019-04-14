package com.example.mazeexplorer

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import com.example.mazeexplorer.mazepieces.Player
import com.example.mazeexplorer.mazepieces.SaveableMazeMap
import kotlinx.android.synthetic.main.activity_maze.*

class Maze : AppCompatActivity() {

    private var saveMap = SaveableMazeMap()

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
                saveMap.map.movePlayerLeft()
                movePlayer()
            }

            override fun onSwipeRight() {
                saveMap.map.movePlayerRight()
                movePlayer()
            }

            override fun onSwipeTop() {
                saveMap.map.movePlayerUp()
                movePlayer()
            }

            override fun onSwipeBottom() {
                saveMap.map.movePlayerDown()
                movePlayer()
            }
        })

        val frag = supportFragmentManager.findFragmentByTag("SavedFragment")

        if(frag == null)
        {
            val size = intent.getIntExtra("size", 5)

            createMaze(size)
        }
        else
        {
            saveMap = frag as SaveableMazeMap
            background.removeView(saveMap.map)
        }

        displayMaze()
        setupPlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        supportFragmentManager.putFragment(outState, "SavedFragment", saveMap)

        background.removeView(saveMap.map)
    }

    private fun createMaze(size : Int)
    {
        saveMap.createMap(this,size,size)
        saveMap.map.id = "MazeMap".hashCode()

        supportFragmentManager.beginTransaction().add(saveMap, "SavedFragment").commit()
    }

    private fun displayMaze() {
        background.addView(saveMap.map)

        val set = ConstraintSet()
        set.clone(background)

        set.constrainWidth(saveMap.map.id, ConstraintSet.WRAP_CONTENT)
        set.centerVertically(saveMap.map.id, ConstraintSet.PARENT_ID)
        set.centerHorizontally(saveMap.map.id, ConstraintSet.PARENT_ID)

        set.applyTo(background)
    }

    private fun setupPlayer() {
        player = Player(this)
        player.id = "Player".hashCode()

        background.addView(player)

        // When the map is ready, perform the move player action.
        saveMap.map.post {movePlayer()}
    }

    private fun movePlayer() {

        val location = saveMap.map.getCurrentPieceLocationCenter()

        // Adjust the player location to be centered in the tile
        player.x = location[0] - player.width / 2
        player.y = location[1] - player.height / 2

        if(saveMap.map.playerVictory())
        {
            val intent = Intent(this@Maze, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
