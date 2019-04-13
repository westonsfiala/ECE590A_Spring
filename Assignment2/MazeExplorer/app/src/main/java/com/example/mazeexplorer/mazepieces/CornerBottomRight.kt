package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class CornerBottomRight(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.corner_bottom_right)
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun isOpenBottom(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return CornerBottomRight(context)
    }
}