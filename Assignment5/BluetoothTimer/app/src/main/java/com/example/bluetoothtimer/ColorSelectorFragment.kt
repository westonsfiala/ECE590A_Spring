package com.example.bluetoothtimer

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_color_selector.*
import kotlin.random.Random

class ColorSelectorFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    var redValue = 0
    var greenValue = 0
    var blueValue = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_color_selector, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Weird hack so that the progress gets updated. Had to find it online.
        // https://stackoverflow.com/questions/9792888/android-seekbar-set-progress-value
        RedScroll.max = 0
        RedScroll.max = 255
        RedScroll.setOnSeekBarChangeListener(this)
        RedScroll.progress = Random.nextInt(0, 255)

        GreenScroll.max = 0
        GreenScroll.max = 255
        GreenScroll.setOnSeekBarChangeListener(this)
        GreenScroll.progress = Random.nextInt(0, 255)

        BlueScroll.max = 0
        BlueScroll.max = 255
        BlueScroll.setOnSeekBarChangeListener(this)
        BlueScroll.progress = Random.nextInt(0, 255)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                   fromUser: Boolean) {

        when
        {
            seekBar.id == RedScroll.id -> { redValue = progress }
            seekBar.id == GreenScroll.id -> { greenValue = progress }
            seekBar.id == BlueScroll.id -> { blueValue = progress }
        }

        colorPreview.setColorFilter(Color.argb(255, redValue, greenValue, blueValue))

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }
}
