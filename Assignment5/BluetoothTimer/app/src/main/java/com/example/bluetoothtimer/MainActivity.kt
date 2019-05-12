package com.example.bluetoothtimer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    var timerRuntime = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TimerDurationSlider.setOnSeekBarChangeListener(this)
        TimerDurationSlider.max = 0
        TimerDurationSlider.max = 50
        TimerDurationSlider.progress = 0

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {
        timerRuntime = progress + 10

        TimerDurationText.text = "Timer Duration: $timerRuntime (seconds)"
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }
}
