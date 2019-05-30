package com.example.customdiceroller.ui.main

import android.app.Dialog
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.example.customdiceroller.R
import kotlin.random.Random

private val MAX_DICE = 100
private val MIN_DICE = 1
private val MAX_MODIFIER = 100
private val START_MODIFIER = 0
private val MIN_MODIFIER = -100

/**
 * A simple [Fragment] subclass.
 * Use the [RollerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RollerFragment : Fragment(), RollFragment.OnFragmentInteractionListener {

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
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val createdView = inflater.inflate(R.layout.fragment_roller, container, false)

        return setupCreatedView(createdView)
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
            val rollFragment = RollFragment.newInstance(dieNumber, dieID, this)
            fragmentManager?.beginTransaction()?.add(line.id, rollFragment, "$dieNumber Tag")?.commit()
            ++columnInRow
        }

        tableLayout.removeAllViews()
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
        numDice = newNumDice
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
        modifier = newModifier
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
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_layout)
        val layout = dialog.findViewById<LinearLayout>(R.id.layout)
        layout.setOnClickListener {
            dialog.dismiss()
        }

        layout.minimumWidth = (view!!.width / 2.5f).toInt()

        val fragmentDice = rollFragment.getDiceNumber()

        val rollName = dialog.findViewById<TextView>(R.id.rollName)

        var tempText = String.format("%dd%d",numDice, fragmentDice)
        if(modifier != 0)
        {
            if(modifier > 0)
            {
                tempText += "+"
            }
            tempText += "$modifier"
        }
        rollName.text = tempText

        var sum = modifier
        var detailString = ""

        for(rollIndex in 1..(numDice))
        {
            val roll = Random.Default.nextInt(1, fragmentDice+1)
            sum += roll
            detailString += "$roll,"
        }

        val correctedString = detailString.removeRange(detailString.length-1,detailString.length)

        val rollTotal = dialog.findViewById<TextView>(R.id.rollTotal)
        rollTotal.text = "$sum"

        val rollDetails = dialog.findViewById<TextView>(R.id.rollDetails)
        rollDetails.text = correctedString

        dialog.show()
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
}
