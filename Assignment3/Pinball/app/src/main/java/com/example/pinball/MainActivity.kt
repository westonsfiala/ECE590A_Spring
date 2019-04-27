package com.example.pinball

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() , SensorEventListener {

    // Accelerometer variables
    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null

    // variables for how often to run the bouncing ball thread
    private lateinit var bounceThread : Thread
    private val sleepTime : Long = 1000/60
    private var yVelocity = 10f
    private val startingXVelocity = 10f

    // If the game is paused or not
    private var runThreads = true
    private var isRunning = true

    // Users score
    private var score = 0
    private var scores : ArrayList<Int> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up all the sub-functions
        readIntent(intent)
        setupAccelerometer()
        setupDisplaySize()
        setupButtons()
        setupBouncingBall()
    }

    override fun onDestroy() {
        super.onDestroy()

        runThreads = false
    }

    private fun readIntent(intent: Intent) {
        yVelocity = intent.getFloatExtra("velocity", 10f)
        scores = intent.getIntegerArrayListExtra("scores")
    }

    private fun setupAccelerometer() {
        // get reference of the service
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun setupDisplaySize() {
        // Force that we have a
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupButtons() {
        exitButton.setOnClickListener {
            val exitIntent = Intent(this@MainActivity, SetupActivity::class.java)
            exitIntent.putIntegerArrayListExtra("scores", scores)
            startActivity(exitIntent)
        }

        pauseButton.setOnClickListener {
            if(isRunning)
            {
                pauseButton.setImageResource(android.R.drawable.ic_media_play)
            }
            else
            {
                pauseButton.setImageResource(android.R.drawable.ic_media_pause)
            }

            isRunning = !isRunning
        }
    }

    private fun setupBouncingBall() {
        bounceThread = Thread {
            greenBall.x = 0f
            greenBall.y = 0f

            var xVelocity = startingXVelocity

            while(runThreads) {
                if (isRunning) {
                    runOnUiThread {

                        if (checkCollision() && yVelocity > 0) {
                            yVelocity *= -1f

                            val xMismatch = redBall.x - greenBall.x

                            if(xMismatch > redBall.width/2 && xVelocity > 0)
                            {
                                xVelocity *= -1f
                                xVelocity -= 2
                            }

                            if(xMismatch < -redBall.width/2 && xVelocity < 0)
                            {
                                xVelocity *= -1f
                                xVelocity += 2
                            }

                            score++
                        }

                        if (greenBall.x < background.left || greenBall.x + greenBall.width > background.right) {
                            xVelocity *= -1f
                        }

                        if (greenBall.y < background.top) {
                            if(xVelocity > startingXVelocity)
                            {
                                xVelocity--
                            }
                            else if (xVelocity < -startingXVelocity)
                            {
                                xVelocity++
                            }

                            yVelocity *= -1f
                        }

                        if (greenBall.y + greenBall.height > background.bottom) {
                            // Game over.
                            isRunning = false

                            val mDialog = AlertDialog.Builder(this)
                            mDialog.setTitle("Game Over")
                            val text = String.format("You scored %d points!", score)
                            mDialog.setMessage(text)
                            mDialog.setPositiveButton("OK") { _, _ -> }
                            mDialog.setOnDismissListener {
                                runThreads = false
                            }
                            mDialog.show()
                        }

                        greenBall.x += xVelocity
                        greenBall.y += yVelocity

                        scoreText.text = String.format("Score = %d", score)
                    }
                }

                // Shoot for 60fps
                Thread.sleep(sleepTime)
            }

            val intent = Intent(this, SetupActivity::class.java)
            if(scores != null)
            {
                scores!!.add(score)
            }
            else
            {
                scores = ArrayList(1)
                scores!!.add(score)
            }
            intent.putIntegerArrayListExtra("scores", scores)
            startActivity(intent)
        }

        bounceThread.start()
    }

    private fun checkCollision() : Boolean
    {
        val redCenter = Point()

        redCenter.x = redBall.x.toInt() + redBall.width/2
        redCenter.y = redBall.y.toInt() + redBall.height/2

        val greenCenter = Point()

        greenCenter.x = greenBall.x.toInt() + greenBall.width/2
        greenCenter.y = greenBall.y.toInt() + greenBall.height/2

        val xDist = pow(redCenter.x - greenCenter.x.toDouble(), 2.toDouble())
        val yDist = pow(redCenter.y - greenCenter.y.toDouble(), 2.toDouble())

        val distance =  sqrt(xDist+yDist)

        return distance < (redBall.width + greenBall.width) / 2
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && isRunning) {
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
