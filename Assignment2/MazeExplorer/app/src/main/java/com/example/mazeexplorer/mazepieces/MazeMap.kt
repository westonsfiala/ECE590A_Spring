package com.example.mazeexplorer.mazepieces

import android.content.Context
import android.widget.TableLayout
import android.widget.TableRow

class MazeMap(context: Context, rows: Int, columns: Int) : TableLayout(context)
{

    var location = 0

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


    fun start() {
        setPiece(0,0)
    }

    private fun setPiece(row: Int, col: Int) {

        // Don't do anything when we are out of bounds.
        if(row < 0 || row > rows || col < 0 || col > columns)
        {
            return
        }

        val line = getChildAt(row) as TableRow

        line.removeViewAt(col)

        val goal = GoalPiece(context)

        line.addView(goal.getImageView(),col)
    }

}