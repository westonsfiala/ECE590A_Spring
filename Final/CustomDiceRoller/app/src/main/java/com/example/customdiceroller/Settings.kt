package com.example.customdiceroller

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_roll.*

class Settings : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

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
        setupShakeSensitivity()
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

    private fun setupShakeSensitivity()
    {
        val shakeSensitivity = preferences.getInt(getString(R.string.shake_sensitivity_key),4)

        // Weird hack so that the progress gets updated. Had to find it online.
        // https://stackoverflow.com/questions/9792888/android-seekbar-set-progress-value
        shakeSensitivityBar.max = 0
        shakeSensitivityBar.max = 9
        shakeSensitivityBar.setOnSeekBarChangeListener(this)
        shakeSensitivityBar.progress = shakeSensitivity
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {

        if(seekBar.id == shakeSensitivityBar.id)
        {
            val editor = preferences.edit()
            editor.putInt(getString(R.string.shake_sensitivity_key), progress)
            editor.apply()
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }
}
