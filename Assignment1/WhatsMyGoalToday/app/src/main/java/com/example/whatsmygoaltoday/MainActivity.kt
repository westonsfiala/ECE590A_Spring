package com.example.whatsmygoaltoday

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SundayButton.setOnClickListener {
            buttonClick(SundayButton.text.toString())
        }

        MondayButton.setOnClickListener {
            buttonClick(MondayButton.text.toString())
        }

        TuesdayButton.setOnClickListener {
            buttonClick(TuesdayButton.text.toString())
        }

        WednesdayButton.setOnClickListener {
            buttonClick(WednesdayButton.text.toString())
        }

        ThursdayButton.setOnClickListener {
            buttonClick(ThursdayButton.text.toString())
        }

        FridayButton.setOnClickListener {
            buttonClick(FridayButton.text.toString())
        }

        SaturdayButton.setOnClickListener {
            buttonClick(SaturdayButton.text.toString())
        }

    }

    private fun buttonClick(buttonText:String) {
        val intent = Intent(this@MainActivity, GoalForDay::class.java)
        intent.putExtra("Day", buttonText)
        startActivity(intent)
    }
}
