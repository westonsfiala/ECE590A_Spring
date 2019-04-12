package com.example.mazeexplorer.mazepieces

import android.content.Context
import android.widget.ImageView
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class HallwayHorizontal(context: Context) : MazePiece {

    private var context = context

    override fun isOpenLeft(): Boolean {
        return true
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun getImageView(): ImageView
    {
        var imageView = ImageView(context)
        imageView.setImageResource(R.drawable.hallway_horizontal)
        return imageView
    }
}