package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class CornerTopRight(context: Context) : MazePiece(context) {

    init{
        setImageResource(R.drawable.corner_top_right)
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun isOpenTop(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return CornerTopRight(context)
    }
}