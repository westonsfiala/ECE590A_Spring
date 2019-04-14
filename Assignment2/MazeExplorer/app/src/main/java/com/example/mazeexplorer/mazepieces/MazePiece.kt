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

    fun numOpenings() : Int {
        var openings = 0

        if(isOpenTop())
        {
            openings++
        }

        if(isOpenBottom())
        {
            openings++
        }

        if(isOpenLeft())
        {
            openings++
        }

        if(isOpenRight())
        {
            openings++
        }

        return openings
    }

    open fun clone(): MazePiece {
        return MazePiece(context)
    }
}