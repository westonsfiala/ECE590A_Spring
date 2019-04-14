package com.example.mazeexplorer

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import kotlinx.android.synthetic.main.activity_congrats.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CongratsActivity : AppCompatActivity() {

    private var mPhotoFile: File? = null

    private lateinit var mCurrentPhotoPath: String
    private val requestTakePhoto = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        val moves = intent.getIntExtra("Moves", 0)

        MoveText.text = "You finished the maze in $moves moves"

        BackButton.setOnClickListener {
            val intent = Intent(this@CongratsActivity, MainActivity::class.java)
            startActivity(intent)
        }

        PictureButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    mPhotoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (mPhotoFile != null) {

                    val photoURI = FileProvider.getUriForFile(this,
                        "com.example.mazeexplorer.fileprovider",
                        mPhotoFile!!)

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, requestTakePhoto)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestTakePhoto && resultCode == RESULT_OK) {
            FileProvider.getUriForFile(this,
                "com.example.mazeexplorer.fileprovider",
                mPhotoFile!!)
            setPic()
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_$timeStamp"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun setPic() {

        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)

        // Determine how much to scale down the image
        //val scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        val scaleFactor = 2

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        VictoryImage.setImageBitmap(bitmap)
    }
}
