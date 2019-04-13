package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class HallwayVertical(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.hallway_vertical)
    }

    override fun isOpenTop(): Boolean {
        return true
    }

    override fun isOpenBottom(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return HallwayVertical(context)
    }
}