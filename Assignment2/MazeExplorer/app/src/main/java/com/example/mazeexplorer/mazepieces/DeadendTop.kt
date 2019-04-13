package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class DeadendTop(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.deadend_top)
    }

    override fun isOpenTop(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return DeadendTop(context)
    }
}