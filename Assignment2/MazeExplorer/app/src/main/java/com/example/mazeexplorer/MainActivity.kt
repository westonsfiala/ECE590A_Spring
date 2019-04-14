package com.example.mazeexplorer

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
