package com.example.whatsmygoaltoday

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_goal_for_day.*

class GoalForDay : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_for_day)

        val intent = this.intent
        val day = intent.getStringExtra("Day")

        setGoalForDay(day)


        BackButton.setOnClickListener {
            val switchIntent = Intent(this@GoalForDay, MainActivity::class.java)
            startActivity(switchIntent)
        }

    }

    private fun setGoalForDay(day:String) {
        GoalText.text = day
    }
}
