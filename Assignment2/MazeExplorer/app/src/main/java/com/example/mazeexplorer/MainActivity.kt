package com.example.mazeexplorer

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val smallSize = 6
    private val mediumSize = 9
    private val largeSize = 12

    private var selectedSize : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StartMazeButton.isEnabled = false

        StartMazeButton.setOnClickListener {
            val intent = Intent(this@MainActivity, Maze::class.java)
            intent.putExtra("size", selectedSize)
            startActivity(intent)
        }

        SmallRadio.setOnClickListener{
            setSize(smallSize)
        }

        MediumRadio.setOnClickListener{
            setSize(mediumSize)
        }

        LargeRadio.setOnClickListener{
            setSize(largeSize)
        }

        HelpButton.text = getString(R.string.how_to_play)

        HelpButton.setOnClickListener{
            val intent = Intent(this@MainActivity, HowToPlayActivity::class.java)
            startActivity(intent)
        }


        if(savedInstanceState != null)
        {
            val savedSize = savedInstanceState.getInt("Size")
            when (savedSize) {
                smallSize -> SmallRadio.performClick()
                mediumSize -> MediumRadio.performClick()
                largeSize -> LargeRadio.performClick()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("Size", selectedSize)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK)
        {
            // Do something
        }
        else
        {
            // Do something else
        }
    }

    private fun setSize(size: Int)
    {
        selectedSize = size
        StartMazeButton.isEnabled = true
    }
}
