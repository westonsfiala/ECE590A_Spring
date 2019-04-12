package com.example.mazeexplorer.mazepieces

import android.content.Context
import android.widget.TableLayout
import android.widget.TableRow

class MazeMap(context: Context, rows: Int, columns: Int) : TableLayout(context)
{
    init
    {
        val piece = Unexplored(context)

        for( row in 0 until columns) {
            val line = TableRow(context)

            for( col in 0 until rows) {
                line.addView(piece.getImageView(), col)
            }

            addView(line,row)
        }
    }

    private val rows = rows
    private val columns = columns



}