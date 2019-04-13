package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class TTop(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.t_top)
    }

    override fun isOpenLeft(): Boolean {
        return true
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun isOpenTop(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return TTop(context)
    }
}