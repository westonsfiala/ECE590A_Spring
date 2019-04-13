package com.example.mazeexplorer.mazepieces

import android.content.Context
import android.widget.ImageView
import com.example.mazeexplorer.R

class Player(context: Context) : ImageView(context){

    init {
        setImageResource(R.drawable.player)
    }
}