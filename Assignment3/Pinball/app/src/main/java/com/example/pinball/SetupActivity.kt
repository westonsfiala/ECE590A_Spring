package com.example.pinball

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity : AppCompatActivity() {

    var scores : ArrayList<Int> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        // Force that we have a good orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        scores = intent.getIntegerArrayListExtra("scores")

        easyButton.performClick()

        startButton.setOnClickListener {
            val gameIntent = Intent(this, MainActivity::class.java)

            when {
                easyButton.isChecked -> gameIntent.putExtra("velocity", 10f)
                mediumButton.isChecked -> gameIntent.putExtra("velocity", 20f)
                else -> gameIntent.putExtra("velocity", 30f)
            }

            gameIntent.putIntegerArrayListExtra("scores", scores)

            startActivity(gameIntent)
        }

        scoreButton.setOnClickListener {
            val intent = Intent(this, ScoreActivity::class.java)

            intent.putIntegerArrayListExtra("scores", scores)

            startActivity(intent)
        }
    }
}
