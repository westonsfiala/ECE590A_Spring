package com.example.customdiceroller

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        preferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        setupShakeSetting()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupShakeSetting()
    {
        val shakeSet = preferences.getBoolean(getString(R.string.shake_preference_key),false)

        shakeToRollSwitch.isChecked = shakeSet

        shakeToRollSwitch.setOnClickListener {
            val editor = preferences.edit()
            editor.putBoolean(getString(R.string.shake_preference_key), shakeToRollSwitch.isChecked)
            editor.apply()
        }
    }
}
