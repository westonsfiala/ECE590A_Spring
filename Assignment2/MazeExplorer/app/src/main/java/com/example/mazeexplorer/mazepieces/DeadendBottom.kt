package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece
import com.example.mazeexplorer.R

class DeadendBottom(context: Context) : MazePiece(context) {

    init{
        setImageResource(R.drawable.deadend_bottom)
    }

    override fun isOpenBottom(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return DeadendBottom(context)
    }
}