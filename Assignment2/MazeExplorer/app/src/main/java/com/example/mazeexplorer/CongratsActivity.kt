package com.example.mazeexplorer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_congrats.*

class CongratsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        val moves = intent.getIntExtra("Moves", 0)

        MoveText.text = "You finished the maze in $moves moves"

        BackButton.setOnClickListener {
            val intent = Intent(this@CongratsActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
