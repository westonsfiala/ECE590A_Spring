package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class DeadendLeft(context: Context) : MazePiece(context) {

    init{
        setImageResource(R.drawable.deadend_left)
    }

    override fun isOpenLeft(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return DeadendLeft(context)
    }
}