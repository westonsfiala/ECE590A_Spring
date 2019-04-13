package edu.uw.pmpee590.filereadwritedemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

/*
 * Save a file on internal storage Your app's internal storage directory is specified
 * by your app's package name in a special location of the Android file system that
 * can be accessed with the following APIs.
 *
 * Unlike the external storage directories, your app does not require any system permissions
 * to read and write to the internal directories returned by these methods.

 * Additional notes:
 *  Passing MODE_PRIVATE makes it private to your app.
  * If your app needs to share private files with other apps, you should instead use a
  * FileProvider with the FLAG_GRANT_READ_URI_PERMISSION.
  *
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    lateinit var filename: String

    fun write_file_internal(view: View) {

        filename = "myfile.txt"
        val fileContents = user_text.text.toString()

        //This is how you write text in the file "manually"
        /*val fileContents = "First Line! \n" +
                            "Second line here!"*/
        this.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }


    fun read_file_internal(view: View) {
        val file = File(this.filesDir, filename)
        val contents = file.readLines() // Read file by lines
        //val contentsAll = file.readText()  // Read all text

        val first_line = contents[0]
        //to read line i:
        //              text_to_show = contents[i]

        text_show.text = first_line.toString()

    }
}