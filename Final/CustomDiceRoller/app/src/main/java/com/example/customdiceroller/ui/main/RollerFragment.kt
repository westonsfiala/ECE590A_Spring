package com.example.customdiceroller.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow

import com.example.customdiceroller.R
import com.example.customdiceroller.RollFragment
import kotlinx.android.synthetic.main.fragment_roller.*


/**
 * A simple [Fragment] subclass.
 * Use the [RollerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RollerFragment : Fragment() {

    private val dice = mapOf("d4" to R.drawable.ic_d4,
        "d6" to R.drawable.ic_perspective_dice_six,
        "d8" to R.drawable.ic_dice_eight_faces_eight,
        "d10" to R.drawable.ic_d10,
        "d12" to R.drawable.ic_d12,
        "d20" to R.drawable.ic_dice_twenty_faces_twenty,
        "d100" to R.drawable.ic_rolling_dice_cup
        )

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
            val name = die.key
            val dieID = die.value
            fragmentManager?.beginTransaction()?.add(line.id, RollFragment.newInstance(name, dieID), "$name Tag")?.commit()
            ++columnInRow
        }

        return createdView
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
