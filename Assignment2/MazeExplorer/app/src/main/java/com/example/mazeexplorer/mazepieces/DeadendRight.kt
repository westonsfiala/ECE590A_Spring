package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class DeadendRight(context: Context) : MazePiece(context) {

    init{
        setImageResource(R.drawable.deadend_right)
    }

    override fun isOpenRight(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return DeadendRight(context)
    }
}