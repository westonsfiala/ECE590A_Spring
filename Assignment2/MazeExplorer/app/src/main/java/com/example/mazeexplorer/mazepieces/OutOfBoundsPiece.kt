package com.example.mazeexplorer.mazepieces

import android.content.Context
import com.example.mazeexplorer.MazePiece

class OutOfBoundsPiece(context: Context) : MazePiece(context) {

    override fun isOpenLeft(): Boolean {
        return false
    }

    override fun isOpenRight(): Boolean {
        return false
    }

    override fun isOpenTop(): Boolean {
        return false
    }

    override fun isOpenBottom(): Boolean {
        return false
    }

    override fun isExplored(): Boolean {
        return true
    }

    override fun clone(): MazePiece {
        return OutOfBoundsPiece(context)
    }
}