package edu.uw.pmpee590.knowyourknotes

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_knotes_details.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.res.Configuration

class KnotesDetails : AppCompatActivity() {
/*

    var figure8_desc:String = "With a long bight in the rope use two double loops to tie a Figure 8 knot. Then pass the end of the original bight under, up, and over the whole knot. Pull it tight to lock the two loops."
    var butterfly_desc: String = "Join the two ends temporarily. Wind the rope around your hand so that the join is by your finger tips. Go around again. Fold the join back and then up under the other ropes. Push the knot off your hand and tighten to see the appearance of the Alpine Butterfly. Finally, release the temporary join."
    var stopper_desc:String = "Form a loop in the rope. Pass the end through it. Pass the end through the loop again. Tighten the knot to make a secure stopper knot."
    var fisherman_desc :String = "Overlap the two ends. Wrap one end around both ropes two full turns. Then pass this end back through these turns and pull tight. Next pass the other end two full turns around both ropes. Pass this end back through and pull tight. Pull on both ropes to tighten the two knots against each other."
    val list = mutableListOf(figure8_desc,butterfly_desc,stopper_desc,fisherman_desc)
*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knotes_details)

        val id = intent.getIntExtra("KEY", R.id.figure8)
        update_knot_info(id)
/*        val id = intent.getIntExtra("KEY", R.id.figure8)
       //var knote_info = ""

        knote_info.text = when (id) {
            R.drawable.figure8 -> list[0]
            R.drawable.butterfly -> list[1]
            R.drawable.fisherman -> list[2]
            R.drawable.stopper -> list[3]

            else
            -> list[1]
        }*/
    }


    fun update_knot_info(id:Int){
/*
        val file_name = when (id) {

            R.drawable.figure8 -> "figure8"
            R.drawable.butterfly -> "butterfly"
            R.drawable.fisherman -> "fisherman"
            R.drawable.stopper -> "stopper"

            else
            -> "butterfly"
        }
*/

        val file_name = getResources().getResourceEntryName(id);
        val textFiledID = resources.getIdentifier(file_name, "raw", packageName)
        val fileRead= resources.openRawResource(textFiledID).bufferedReader().readText()
        knote_info.text = fileRead

   }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            butterfly.isChecked=true
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            butterfly.isChecked=true
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }
    fun onclickSubmit(view: View) {
        val user_text = user_description.text.toString()
        val intent = Intent()
        intent.putExtra("KEY_D", user_text)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
