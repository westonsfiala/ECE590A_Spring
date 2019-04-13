package com.example.mazeexplorer.mazepieces

import android.content.Context
import android.widget.TableLayout
import android.widget.TableRow
import com.example.mazeexplorer.MazePiece
import kotlin.random.Random

class MazeMap(context: Context, private val rows: Int, private val columns: Int) : TableLayout(context)
{
    private val startTile = Fourway(context)
    private val unexplored = Unexplored(context)

    private val allMazePieces : Array <MazePiece> = arrayOf(
        CornerBottomLeft(context),
        CornerBottomRight(context),
        CornerTopLeft(context),
        CornerTopRight(context),
        DeadendBottom(context),
        DeadendTop(context),
        DeadendLeft(context),
        DeadendRight(context),
        startTile,
        GoalPiece(context),
        HallwayHorizontal(context),
        HallwayVertical(context),
        TBottom(context),
        TLeft(context),
        TTop(context),
        TRight(context)
    )

    init
    {
        blankMap()
        start()
    }

    private fun blankMap() {

        val piece = unexplored

        for( row in 0 until columns) {
            val line = TableRow(context)

            for( col in 0 until rows) {
                line.addView(piece.clone(), col)
            }

            addView(line,row)
        }
    }

    private fun start() {

        // First piece will be somewhere in the middle.
        val row = Random.nextInt(1, rows-1)
        val column = Random.nextInt(1, rows-1)
        val piece = Fourway(context)

        setPiece(row,column, piece)
        revealAdjacent(row,column)
    }

    private fun setPiece(row: Int, col: Int, piece: MazePiece) {

        // Don't do anything when we are out of bounds.
        if(!isInBounds(row,col))
        {
            return
        }

        // Swap out the piece for something we care about.
        val line = getChildAt(row) as TableRow
        line.removeViewAt(col)
        line.addView(piece.clone(),col)
    }

    // Get the piece that is placed at the the given coordinates.
    // If out of bounds, returns unexplored
    private fun getPiece(row: Int, col: Int) : MazePiece {

        // Return unexplored when out of bounds
        if(!isInBounds(row,col))
        {
            return unexplored
        }

        val line = getChildAt(row) as TableRow
        return line.getChildAt(col) as MazePiece
    }

    // Reveal all of the pieces that are adjacent to the known piece.
    private fun revealAdjacent(row: Int, col: Int) {
        revealTop(row, col)
        //revealBottom(row, col)
        //revealLeft(row, col)
        //revealRight(row, col)
    }

    // Reveal the piece directly above the current piece.
    private fun revealTop(row: Int, col: Int) {

        // Can't reveal outside of the bounds
        if(!isInBounds(row - 1, col))
        {
            return
        }

        // Current piece doesn't allow revealing top
        val knownPiece = getPiece(row, col)
        if(!knownPiece.isOpenTop())
        {
            return
        }

        // The top piece is already explored, nothing to reveal
        val piece = getPiece(row - 1, col)
        if(piece.isExplored())
        {
            return
        }

        // Get a new piece and place it
        setPiece(row-1, col, getNewPiece(row-1, col))
    }

    private fun getNewPiece(row: Int, col: Int) : MazePiece {
        val validPieces: MutableList<MazePiece> = mutableListOf()

        val mustHaveOpenTop = mustHaveTopOpening(row, col)
        val cannotHaveOpenTop = cantHaveTopOpening(row, col)

        for(piece in allMazePieces)
        {
            if(piece.isOpenTop() && cannotHaveOpenTop || !piece.isOpenTop() && mustHaveOpenTop) {
                continue
            }

            validPieces.add(piece)
        }

        // If no valid pieces were found, ditch out.
        if(validPieces.size == 0)
        {
            return unexplored
        }

        // Return a random valid piece.
        val randInt = Random.nextInt(0, validPieces.size)
        return validPieces[randInt]
    }

    // Checks if the piece at the given coordinates must have a top opening
    private fun mustHaveTopOpening(row: Int, col: Int) : Boolean {

        // You do not need a top opening if you are at the edge of map
        if(!isInBounds(row-1,col))
        {
            return false
        }

        // If the piece is not explored, no need to force a top
        val piece = getPiece(row-1,col)
        if(!piece.isExplored())
        {
            return false
        }

        // If the piece above is open to the bottom, we are required to have a top opening
        return piece.isOpenBottom()
    }

    // Checks if the piece at the given coordinates can't have a top opening
    private fun cantHaveTopOpening(row: Int, col: Int) : Boolean {

        // You can never go out of bounds
        if(!isInBounds(row-1,col))
        {
            return true
        }

        // If the piece is not explored, not need to force no top
        val piece = getPiece(row-1,col)
        if(!piece.isExplored())
        {
            return false
        }

        // If the piece above is not open on the bottom, we are required to not have a top opening
        return !piece.isOpenBottom()
    }

    private fun isInBounds(row: Int, col: Int) : Boolean {
        return row in 0..(rows - 1) && col in 0..(rows-1)
    }



}