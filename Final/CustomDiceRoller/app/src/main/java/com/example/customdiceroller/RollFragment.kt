package com.example.customdiceroller


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_roll.*

private const val ARG_NAME = "name"
private const val ARG_IMAGE_ID = "imageID"

/**
 * A simple [Fragment] subclass.
 * Use the [RollFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RollFragment : Fragment() {
    private var rollName: String = "TEMP"
    private var imageID: Int = R.drawable.ic_launcher_foreground

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val blah = it.getString(ARG_NAME)

            if(blah != null)
            {
                rollName = blah
            }
            imageID = it.getInt(ARG_IMAGE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val createdView = inflater.inflate(R.layout.fragment_roll, container, false)

        createdView.findViewById<ImageView>(R.id.displayImage).setImageResource(imageID)
        createdView.findViewById<TextView>(R.id.displayText).text = rollName

        return createdView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        layout.setOnClickListener {
            val dialog = Dialog(context!!)
            dialog.setContentView(R.layout.dialog_layout)
            val layout = dialog.findViewById<LinearLayout>(R.id.layout)
            layout.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param rollName Parameter 1.
         * @param imageID Parameter 2.
         * @return A new instance of fragment RollFragment.
         */
        @JvmStatic
        fun newInstance(rollName: String, imageID: Int) =
            RollFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, rollName)
                    putInt(ARG_IMAGE_ID, imageID)
                }
            }
    }
}
