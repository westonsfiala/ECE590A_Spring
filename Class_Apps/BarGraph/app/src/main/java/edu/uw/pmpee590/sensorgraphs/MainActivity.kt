package edu.uw.pmpee590.sensorgraphs

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.DefaultLabelFormatter
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {

    private  var mSeries: LineGraphSeries<DataPoint>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lineplot(mGraphX)
        mSeries = LineGraphSeries()
        initGraphRT(mGraphX,mSeries!!)
    }


    private fun initGraphRT(mGraph:GraphView,mSeriesX :LineGraphSeries<DataPoint>){
        mGraph.viewport.isXAxisBoundsManual = false
        mGraph.viewport.isYAxisBoundsManual = false
        //mGraph.viewport.setMinY(0.0)
        //mGraph.viewport.setMaxY(10.0)

        mGraph.getGridLabelRenderer().setLabelVerticalWidth(100)
        setLabelsFormat(mGraph)
        mGraph.addSeries(mSeriesX)

    }

    /*    Formatting the plot
*  - Set the label "Time" to the X axis
*  - Set the maximum fraction and integer digits*/
    fun setLabelsFormat(mGraph:GraphView){
        mGraph.getGridLabelRenderer().setVerticalAxisTitle("Vertical Axis name")
        mGraph.getGridLabelRenderer().setHorizontalAxisTitle("Horizontal Axis name")

    }


    fun barplot(mGraph:GraphView){
        val series = BarGraphSeries(
            arrayOf(
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0)
        )
        )
        mGraph.addSeries(series)

// styling
        //set random colors
        series.setValueDependentColor { data ->
            Color.rgb(
                data.x.toInt() * 255 / 4,
                Math.abs(data.y * 255 / 6).toInt(),
                100
            )
        }

        series.spacing = 10

// draw values on top
        series.isDrawValuesOnTop = true
        series.valuesOnTopColor = Color.RED
    }


    fun lineplot(mGraph:GraphView){
       val series = LineGraphSeries(
            arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
            )
        )
        mGraph.addSeries(series)
    }






}
