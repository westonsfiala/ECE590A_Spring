package com.example.pinball

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , SensorEventListener {

    private var mSensorManager : SensorManager?= null
    private var mAccelerometer : Sensor?= null

    var isRunning = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up all the sub-functions
        setupAccelerometer()
        setupDisplaySize()
        setupBouncingBall()
    }

    private fun setupAccelerometer() {
        // get reference of the service
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // setup the window
    }

    private fun setupDisplaySize() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    private fun setupBouncingBall() {
        Thread {
            greenBall.x = 0f
            greenBall.y = 0f

            var xVelocity = 10f
            var yVelocity = 10f

            while(isRunning)
            {
                greenBall.x += xVelocity
                greenBall.y += yVelocity

                if(greenBall.x < background.left || greenBall.x + greenBall.width > background.right)
                {
                    xVelocity *= -1f
                }

                if(greenBall.y < background.top || greenBall.y + greenBall.height > background.bottom)
                {
                    yVelocity *= -1f
                }

                // Shoot for 60fps
                Thread.sleep(1000/60)
            }
        }.start()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val xAxis = event.values[0]

            var xDoubled = xAxis * xAxis

            if(xAxis < 0)
            {
                xDoubled *= -1
            }

            redBall.x -= xDoubled

            if(redBall.x < background.left)
            {
                redBall.x = 0f
            }
            else if(redBall.x + redBall.width > background.right)
            {
                redBall.x = background.right.toFloat() - redBall.width
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this,mAccelerometer,
            SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }
}
