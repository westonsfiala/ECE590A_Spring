package com.example.customdiceroller.ui.main


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.customdiceroller.R



class HistoryStamp(private val rollResult: Int,
                   private val rollText: String,
                   private val rollDetails: String,
                   private val timeStamp: String)
{
    fun createView(inflater: LayoutInflater) : View
    {
        val createdView = inflater.inflate(R.layout.roll_history_layout, null)

        createdView.findViewById<TextView>(R.id.rollResultText).text = "$rollResult"
        createdView.findViewById<TextView>(R.id.rollTitleText).text = rollText
        createdView.findViewById<TextView>(R.id.rollDetailsText).text = rollDetails
        createdView.findViewById<TextView>(R.id.rollTimeStampText).text = timeStamp

        return createdView
    }
}

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HistoryFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        pageViewModel.rollHistory.observe(this, Observer<MutableList<HistoryStamp>> {
            val historyLayout = view?.findViewById<LinearLayout>(R.id.historyLayout)

            historyLayout?.removeAllViews()

            for(history in it!!)
            {
                historyLayout?.addView(history.createView(LayoutInflater.from(context)))
            }
        })

        val createdView = inflater.inflate(R.layout.fragment_history, container, false)

        pageViewModel.addRollHistory(HistoryStamp(5555,"name", "details", "time"))

        // Testing for the layout.
        //createdView.findViewById<LinearLayout>(R.id.historyLayout).addView(HistoryStamp(5555,"name", "details", "time").createView(inflater))

        // Inflate the layout for this fragment
        return createdView
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HistoryFragment.
         */
        @JvmStatic
        fun newInstance() =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
