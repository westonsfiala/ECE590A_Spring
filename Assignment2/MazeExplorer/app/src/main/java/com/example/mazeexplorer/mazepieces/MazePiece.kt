package com.example.mazeexplorer

import android.content.Context
import android.widget.ImageView

open class MazePiece(context: Context) : ImageView(context){

    open fun isOpenLeft(): Boolean {
        return false
    }

    open fun isOpenRight(): Boolean {
        return false
    }

    open fun isOpenTop(): Boolean {
        return false
    }

    open fun isOpenBottom(): Boolean {
        return false
    }

    open fun isExplored(): Boolean {
        return true
    }

    open fun clone(): MazePiece {
        return MazePiece(context)
    }
}