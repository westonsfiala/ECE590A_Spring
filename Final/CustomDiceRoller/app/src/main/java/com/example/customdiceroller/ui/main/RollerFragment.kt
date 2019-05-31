package com.example.customdiceroller.ui.main

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock.sleep
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.customdiceroller.MainActivity

import com.example.customdiceroller.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

private const val MAX_DICE = 100
private const val MIN_DICE = 1
private const val MAX_MODIFIER = 100
private const val START_MODIFIER = 0
private const val MIN_MODIFIER = -100

/**
 * A simple [Fragment] subclass.
 * Use the [RollerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RollerFragment : Fragment(), RollFragment.OnFragmentInteractionListener, SensorEventListener {

    private lateinit var pageViewModel: PageViewModel

    private var xAccel = 0.0f
    private var yAccel = 0.0f
    private var zAccel = 0.0f

    // Accelerometer variables
    private var mSensorManager : SensorManager?= null
    private var mAccelerometer : Sensor?= null

    private var testDie : shakeDie?= null
    private var runThread = false

    private val dice = mapOf(
        4 to R.drawable.ic_d4,
        6 to R.drawable.ic_perspective_dice_six,
        8 to R.drawable.ic_dice_eight_faces_eight,
        10 to R.drawable.ic_d10,
        12 to R.drawable.ic_d12,
        20 to R.drawable.ic_dice_twenty_faces_twenty,
        100 to R.drawable.ic_rolling_dice_cup
        )

    private var numDice = 1
    private var modifier = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = activity?.run {
            ViewModelProviders.of(this).get(PageViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val createdView = inflater.inflate(R.layout.fragment_roller, container, false)

        setupObservers()
        setupAccelerometer()
        return setupCreatedView(createdView)
    }

    private fun setupObservers()
    {
        pageViewModel.numDice.observe(this, Observer<Int> {
            numDice = it!!
            updateNumDiceText(view!!)
        })

        pageViewModel.modifier.observe(this, Observer<Int> {
            modifier = it!!
            updateModifierText(view!!)
        })
    }

    private fun setupAccelerometer() {
        // get reference of the service
        mSensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun setupCreatedView(view: View) : View
    {
        setupDiceButtons(view)
        setupUpAndDownButtons(view)
        return view
    }

    private fun setupDiceButtons(view: View)
    {
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)

        val layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT, 1.0f
        )

        var rowInTable = 0
        var columnInRow = 0
        var line = TableRow(context)
        line.layoutParams = layoutParams
        tableLayout.addView(line, rowInTable)
        line.id = "$rowInTable Line".hashCode()
        for(die in dice)
        {
            if(columnInRow >= 4)
            {
                line = TableRow(context)
                line.layoutParams = layoutParams
                ++rowInTable
                line.id = "$rowInTable Line".hashCode()
                columnInRow = 0
                tableLayout.addView(line, rowInTable)
            }
            val dieNumber = die.key
            val dieID = die.value
            val fragmentTag = "$dieNumber Tag"
            val oldFragment = fragmentManager?.findFragmentByTag(fragmentTag)

            if(oldFragment != null) {
                fragmentManager?.beginTransaction()?.remove(oldFragment)?.commit()
            }

            val rollFragment = RollFragment.newInstance(dieNumber, dieID, this)
            fragmentManager?.beginTransaction()?.add(line.id, rollFragment, fragmentTag)?.commit()
            ++columnInRow

        }
    }

    private fun setupUpAndDownButtons(view: View)
    {
        val diceUpBut = view.findViewById<ImageButton>(R.id.diceUpButton)
        diceUpBut.setOnClickListener {
            setNumDice(numDice + 1)
        }

        diceUpBut.setOnLongClickListener {
            setNumDice(MAX_DICE)
            true
        }

        val diceDownBut = view.findViewById<ImageButton>(R.id.diceDownButton)
        diceDownBut.setOnClickListener {
            setNumDice(numDice - 1)
        }

        diceDownBut.setOnLongClickListener {
            setNumDice(MIN_DICE)
            true
        }

        val modifierUpBut = view.findViewById<ImageButton>(R.id.modifierUpButton)
        modifierUpBut.setOnClickListener {
            setModifier(modifier + 1)
        }

        modifierUpBut.setOnLongClickListener {
            if(modifier >= 0)
            {
                setModifier(MAX_MODIFIER)
            }
            else
            {
                setModifier(START_MODIFIER)
            }
            true
        }

        val modifierDownBut = view.findViewById<ImageButton>(R.id.modifierDownButton)
        modifierDownBut.setOnClickListener {
            setModifier(modifier - 1)
        }

        modifierDownBut.setOnLongClickListener {
            if(modifier <= 0)
            {
                setModifier(MIN_MODIFIER)
            }
            else
            {
                setModifier(START_MODIFIER)
            }
            true
        }

        updateNumDiceText(view)
        updateModifierText(view)
    }

    private fun setNumDice(newNumDice: Int)
    {
        pageViewModel.setNumDice(newNumDice)
        if(numDice < 1)
        {
            numDice = 1
        }
        else if(numDice > 100)
        {
            numDice = 100
        }
        updateNumDiceText(view!!)
    }

    private fun updateNumDiceText(view: View)
    {
        val diceText = view.findViewById<TextView>(R.id.numDiceText)
        diceText.text = String.format("%dd",numDice)
    }

    private fun setModifier(newModifier: Int)
    {
        pageViewModel.setModifier(newModifier)
        if(modifier > 100)
        {
            modifier = 100
        }
        else if(modifier < -100)
        {
            modifier = -100
        }
        updateModifierText(view!!)
    }

    private fun updateModifierText(view: View)
    {
        val modifierText = view.findViewById<TextView>(R.id.modifierText)
        when
        {
            modifier == 0 -> modifierText.text = "0"
            modifier > 0 -> modifierText.text = String.format("+%d",modifier)
            modifier < 0 -> modifierText.text = String.format("%d",modifier)
        }
    }

    override fun onRollClicked(rollFragment: RollFragment)
    {
        val mainActivity = activity!! as MainActivity

        if(mainActivity.shakeToRoll())
        {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.diceroll_layout)
            val layout = dialog.findViewById<ConstraintLayout>(R.id.rollArea)
            layout.setOnClickListener {
                dialog.dismiss()
            }

            testDie = shakeDie(rollFragment.getDiceImageID())

            runThread = true
            dialog.setOnDismissListener {
                runThread = false
            }

            diceRollThreadRunner(dialog)

            //val dialog = AlertDialog.Builder(context!!).create()
            //dialog.setTitle("Implement Roller")
            //dialog.setMessage("xVel = $xVelocity \n" +
            //        "yVel = $yVelocity \n" +
            //        "zVel = $zVelocity")

            //dialog.setCancelable(false)

            dialog.show()
        }
        else
        {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.dialog_layout)
            val layout = dialog.findViewById<LinearLayout>(R.id.rollerLayout)
            layout.setOnClickListener {
                dialog.dismiss()
            }

            layout.minimumWidth = (view!!.width / 2.5f).toInt()

            val fragmentDice = rollFragment.getDiceNumber()

            val rollName = dialog.findViewById<TextView>(R.id.rollName)

            var rollDisplay = String.format("%dd%d", numDice, fragmentDice)
            if (modifier != 0) {
                if (modifier > 0) {
                    rollDisplay += "+"
                }
                rollDisplay += "$modifier"
            }
            rollName.text = rollDisplay

            var sum = modifier
            var detailString = ""

            for (rollIndex in 1..(numDice)) {
                val roll = Random.Default.nextInt(1, fragmentDice + 1)
                sum += roll
                detailString += "$roll, "
            }

            val correctedString = detailString.removeRange(detailString.length - 2, detailString.length)

            val rollTotal = dialog.findViewById<TextView>(R.id.rollTotal)
            rollTotal.text = "$sum"

            val rollDetails = dialog.findViewById<TextView>(R.id.rollDetails)
            rollDetails.text = correctedString

            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val formattedDate = formatter.format(time)

            pageViewModel.addRollHistory(HistoryStamp.newInstance(sum, rollDisplay, correctedString, formattedDate))

            dialog.show()
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            xAccel = event.values[0]
            yAccel = event.values[1]
            zAccel = event.values[2]
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

    private fun diceRollThreadRunner(rollArea: Dialog)
    {
        val diceShakerThread = Thread {

            val rollContainer = rollArea.findViewById<ConstraintLayout>(R.id.rollArea)
            if(testDie != null && rollContainer != null) {

                activity?.runOnUiThread {
                    rollContainer.addView(testDie?.getImage())
                }

                testDie!!.getImage().x = Random.nextFloat() * rollContainer.width.toFloat()
                testDie!!.getImage().y = Random.nextFloat() * rollContainer.height.toFloat()

                while (runThread) {
                    val currentX = testDie!!.getImage().x
                    val currentRight = currentX + testDie!!.getImage().width
                    val currentY = testDie!!.getImage().y
                    val currentBottom = currentY + testDie!!.getImage().height

                    if(currentX < 0 || currentRight > rollContainer.width)
                    {
                        testDie!!.xVelocity *= -1
                    }

                    if(currentY < 0 || currentBottom > rollContainer.height)
                    {
                        testDie!!.yVelocity *= -1
                    }

                    activity?.runOnUiThread {
                        testDie!!.getImage().x = currentX + testDie!!.xVelocity
                        testDie!!.getImage().y = currentY + testDie!!.yVelocity
                    }

                    sleep(50)
                }

                activity?.runOnUiThread {
                    rollContainer.removeView(testDie!!.getImage())
                }
            }
        }

        diceShakerThread.start()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment RollerFragment.
         */
        @JvmStatic
        fun newInstance() =
            RollerFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                }
            }
    }

    inner class shakeDie(private var dieImageID : Int)
    {
        var xVelocity = 0
        var yVelocity = 0
        var spinVelocity = 0
        private var dieView : ImageView ?= null

        init {
            xVelocity = Random.nextInt(-50, 50)
            yVelocity = Random.nextInt(-50,50)
            spinVelocity = Random.nextInt(-10,10)
        }

        fun getImage() : ImageView
        {
            if(dieView == null)
            {
                dieView = ImageView(context)
                dieView?.setImageResource(dieImageID)
            }

            return dieView!!
        }
    }
}
