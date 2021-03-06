package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class Unexplored(context: Context) : MazePiece(context) {

    init {
        setImageResource(R.drawable.unexplored)
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

    override fun isOpenBottom(): Boolean {
        return true
    }

    override fun isExplored(): Boolean {
        return false
    }

    override fun clone(): MazePiece {
        return Unexplored(context)
    }
}