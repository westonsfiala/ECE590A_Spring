package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class HallwayHorizontal(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.hallway_horizontal)
    }

    override fun isOpenLeft(): Boolean {
        return true
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return HallwayHorizontal(context)
    }
}