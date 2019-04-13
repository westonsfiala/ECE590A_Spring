package edu.uw.pmpee590.knowyourknotes


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.content.res.Configuration


class MainActivity : AppCompatActivity() {

    private var mKnoteID:Int = R.drawable.figure8
    private var mm :String = "this is me"
    /*
     * Called when the activity first gets created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        butterfly.isChecked = true
        user_info.text=getString(R.string.initial_info)
    }


    override  fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            butterfly.isChecked = true


        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            butterfly.isChecked = true
        }
    }

    fun knoteclick(view: View){
        //Create intent

        val i = Intent(this, KnotesDetails::class.java)
        i.putExtra("KEY", mKnoteID)
        startActivityForResult(i, PICK_KNOTE_REQUEST)    }

    /*
      * This method is called when the user chooses one of the knote radio buttons.
      * In this code we set which knote image is visible on the screen in the ImageView.
      */
    fun pickKnote(view: View) {
        updateKnoteImage(view)
    }
    /*
     * Called by various event handlers to update which knote image is showing
     * based on which radio button is currently checked.
     */
    private fun updateKnoteImage(view: View) {
        mKnoteID = when (view) {
            figure8 -> R.drawable.figure8
            butterfly -> R.drawable.butterfly
            fisherman -> R.drawable.fisherman
            stopper -> R.drawable.stopper
            else
            -> R.drawable.figure8
        }
        knote.setImageResource(mKnoteID)
        display_myDialog(mKnoteID)
    }

    fun display_myDialog(resourceID:Int){
        //make a dialog
        val name = getResources().getResourceEntryName(resourceID);
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle("Info about $name")
        mDialog.setMessage("This is a Dialog!")
        mDialog.setPositiveButton("OK"){_, _ ->} //you can attach a listener to respond to the OK button
        mDialog.show()
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == PICK_KNOTE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                val info_rec = data!!.getStringExtra("KEY_D")
                user_info.text = info_rec
            }
        }
    }
    companion object {
        // these "request codes" are used to identify sub-activities that return results
        private val PICK_KNOTE_REQUEST = 1234
    }
}






