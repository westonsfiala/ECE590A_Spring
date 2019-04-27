package com.example.pinball

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        // Force that we have a
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        easyButton.performClick()

        startButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)

            when {
                easyButton.isChecked -> intent.putExtra("velocity", 10f)
                mediumButton.isChecked -> intent.putExtra("velocity", 20f)
                else -> intent.putExtra("velocity", 30f)
            }

            startActivity(intent)
        }
    }
}
