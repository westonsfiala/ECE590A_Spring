package com.example.customdiceroller.ui.main

import android.app.Dialog
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

import com.example.customdiceroller.R
import kotlin.random.Random


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

        val tableLayout = createdView.findViewById<TableLayout>(R.id.tableLayout)

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

        return createdView
    }

    override fun onRollClicked(rollFragment: RollFragment)
    {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.dialog_layout)
        val layout = dialog.findViewById<LinearLayout>(R.id.layout)
        layout.setOnClickListener {
            dialog.dismiss()
        }

        layout.minimumWidth = view!!.width / 2

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

        var sum = 0
        var detailString = ""

        for(rollIndex in 1..(numDice))
        {
            val roll = Random.Default.nextInt(1, fragmentDice+1) + modifier
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
