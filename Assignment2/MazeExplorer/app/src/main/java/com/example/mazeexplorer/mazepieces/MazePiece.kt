package com.example.mazeexplorer

import android.widget.ImageView

interface MazePiece {

    fun isOpenLeft(): Boolean {
        return false
    }

    fun isOpenRight(): Boolean {
        return false
    }

    fun isOpenTop(): Boolean {
        return false
    }

    fun isOpenBottom(): Boolean {
        return false
    }

    fun getImageView(): ImageView


}