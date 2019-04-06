package com.example.whatsmygoaltoday

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random
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

    var sunday_goals = arrayOf(
        "Do your EE590A homework",
        "Play D&D with friends",
        "Submit your application for a new job",
        "Play Gloomhaven with friends",
        "Sleep in till noon")

    var monday_goals = arrayOf(
        "Go to work",
        "Play D&D with friends",
        "Do hip stretches",
        "Go to yoga class",
        "Pet your cat"
    )

    var tuesday_goals = arrayOf(
        "Go to work",
        "Play D&D with friends",
        "Go on a walk at lunch",
        "Do hip strengthening exercises",
        "Go to bed at a reasonable hour"
    )

    var wednesday_goals = arrayOf(
        "Go to work",
        "Pet your cat",
        "Make lunch instead of going out",
        "Go to yoga class",
        "Turn in your EE590A homework"
    )

    var thursday_goals = arrayOf(
        "Go to work",
        "Go to EE590A class",
        "Go on a walk at lunch",
        "Pet your cat",
        "Do hip strengthening exercises"
    )

    var friday_goals = arrayOf(
        "Go to work",
        "Present the sprint review",
        "Play video games",
        "Go to dinner with girlfriend",
        "Do hip stretches"
    )

    var saturday_goals = arrayOf(
        "Sleep in till noon",
        "Do EE590A homework",
        "Clean the house",
        "Pet your cat",
        "Go on a walk around the lake"
    )

    private fun setGoalForDay(day:String) {

        when(day) {
            getString(R.string.sunday) -> {
                val textIdx = Random.nextInt(0, sunday_goals.size)
                GoalText.text = sunday_goals[textIdx]
            }
            getString(R.string.monday) -> {
                val textIdx = Random.nextInt(0, monday_goals.size)
                GoalText.text = monday_goals[textIdx]
            }
            getString(R.string.tuesday) -> {
                val textIdx = Random.nextInt(0, tuesday_goals.size)
                GoalText.text = tuesday_goals[textIdx]
            }
            getString(R.string.wednesday) -> {
                val textIdx = Random.nextInt(0, wednesday_goals.size)
                GoalText.text = wednesday_goals[textIdx]
            }
            getString(R.string.thursday) -> {
                val textIdx = Random.nextInt(0, thursday_goals.size)
                GoalText.text = thursday_goals[textIdx]
            }
            getString(R.string.friday) -> {
                val textIdx = Random.nextInt(0, friday_goals.size)
                GoalText.text = friday_goals[textIdx]
            }
            getString(R.string.saturday) -> {
                val textIdx = Random.nextInt(0, saturday_goals.size)
                GoalText.text = saturday_goals[textIdx]
            }
            else -> GoalText.text = "Fix your broken code"
        }
    }
}
