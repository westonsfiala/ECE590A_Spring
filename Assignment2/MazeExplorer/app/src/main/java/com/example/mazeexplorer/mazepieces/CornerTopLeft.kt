package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class CornerTopLeft(context: Context) : MazePiece(context) {

    init{
        setImageResource(R.drawable.corner_top_left)
    }

    override fun isOpenLeft(): Boolean {
        return true
    }

    override fun isOpenTop(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return CornerTopLeft(context)
    }
}