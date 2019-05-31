package com.example.customdiceroller.ui.main


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.customdiceroller.R



class HistoryStamp(val rollResult: Int, val rollText: String, val rollDetails: String, val timeStamp: String)

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


        pageViewModel.rollHistory.observe(this, Observer<List<HistoryStamp>> {
            val historyLayout = view?.findViewById<LinearLayout>(R.id.historyLayout)

            historyLayout?.removeAllViews()

        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
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
