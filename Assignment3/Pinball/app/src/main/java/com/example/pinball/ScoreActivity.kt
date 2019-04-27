package com.example.pinball

import android.content.Intent
import android.content.pm.ActivityInfo
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_score.*
import java.util.*
import kotlin.math.max

class ScoreActivity : AppCompatActivity() {
    private var scores : ArrayList<Int> ?= null

    private var maxScore = 0
    private var maxScoreY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Force that we have a good orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        backButton.setOnClickListener {
            val intent = Intent(this, SetupActivity::class.java)
            intent.putIntegerArrayListExtra("scores", scores)
            startActivity(intent)
        }

        scores = intent.getIntegerArrayListExtra("scores")

        if(scores == null)
        {
            scores = ArrayList(0)
        }

        val arrayMap = scoresToPlotable()

        plotScores(arrayMap)
    }

    private fun scoresToPlotable() : BarGraphSeries<DataPoint>
    {
        val map = sortedMapOf<Int,Int>()

        for(value in scores!!)
        {
            if(map.containsKey(value))
            {
                map[value] = map[value]!!.plus(1)
            }
            else
            {
                map[value] = 1
            }

            maxScore = max(maxScore, value)
            maxScoreY = max(maxScoreY, map[value]!!)
        }

        val plotable = BarGraphSeries<DataPoint>()

        for(key in map.keys)
        {
            plotable.appendData(DataPoint(key.toDouble(), map[key]!!.toDouble()), true, 100)
        }


        //set random colors
        plotable.setValueDependentColor { data ->
            Color.rgb(
                data.x.toInt() * 255 / 4,
                Math.abs(data.y * 255 / 6).toInt(),
                100
            )
        }

        plotable.spacing = 75

        // draw values on top
        plotable.isDrawValuesOnTop = true
        plotable.valuesOnTopColor = Color.RED

        return plotable
    }

    private fun plotScores(series : BarGraphSeries<DataPoint>)
    {
        mGraphX.viewport.isXAxisBoundsManual = true
        mGraphX.viewport.isYAxisBoundsManual = true
        mGraphX.viewport.setMinX(0.0)
        mGraphX.viewport.setMaxX(maxScore.toDouble().plus(1.0))
        mGraphX.viewport.setMinY(0.0)
        mGraphX.viewport.setMaxY(maxScoreY.toDouble().plus(1.0))
        mGraphX.gridLabelRenderer.verticalAxisTitle = "Times"
        mGraphX.gridLabelRenderer.horizontalAxisTitle = "Score"

        mGraphX.addSeries(series)
    }
}
