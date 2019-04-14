package com.example.mazeexplorer.mazepieces

import android.support.v4.app.Fragment
import android.content.Context
import android.widget.TableLayout
import android.widget.TableRow
import com.example.mazeexplorer.MazePiece
import kotlin.random.Random
import android.os.Bundle


class SaveableMazeMap : Fragment() {

    // this method is only called once for this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // retain this fragment
        retainInstance = true
    }

    // data object we want to retain
    lateinit var map: MazeMap

    fun createMap(context: Context, rows: Int, columns: Int) {

        // data object we want to retain
        map = MazeMap(context, rows, columns)
    }
}

class MazeMap(context: Context, private val rows: Int, private val columns: Int) : TableLayout(context) {

    private var playerRow = 0
    private var playerCol = 0

    private var goalRow = -1
    private var goalCol = -1

    private var moves = 0

    private val startTile = Fourway(context)
    private val unexplored = Unexplored(context)
    private val goalPiece = GoalPiece(context)
    private val outOfBoundsPiece = OutOfBoundsPiece(context)

    private val allMazePieces: Array<MazePiece> = arrayOf(
        CornerBottomLeft(context),
        CornerBottomRight(context),
        CornerTopLeft(context),
        CornerTopRight(context),
        DeadendBottom(context),
        DeadendTop(context),
        DeadendLeft(context),
        DeadendRight(context),
        startTile,
        HallwayHorizontal(context),
        HallwayVertical(context),
        TBottom(context),
        TLeft(context),
        TTop(context),
        TRight(context)
    )

    init {
        blankMap()
        start()
    }

    fun playerVictory() : Boolean {
        return playerRow == goalRow && playerCol == goalCol
    }

    fun getMoves() : Int {return moves}

    fun getCurrentPieceLocationCenter(): FloatArray {
        val location = FloatArray(2)

        if (!isInBounds(playerRow, playerCol)) {
            return floatArrayOf(0.0f, 0.0f)
        }

        val piece = getPiece(playerRow, playerCol)
        val line = piece.parent as TableRow

        location[0] = x + line.x + piece.x + piece.width / 2
        location[1] = y + line.y + piece.y + piece.height / 2

        return location
    }

    fun movePlayerUp() {
        if (getPiece(playerRow, playerCol).isOpenTop()) {
            movePlayer(-1, 0)
        }
    }

    fun movePlayerDown() {
        if (getPiece(playerRow, playerCol).isOpenBottom()) {
            movePlayer(1, 0)
        }
    }

    fun movePlayerLeft() {
        if (getPiece(playerRow, playerCol).isOpenLeft()) {
            movePlayer(0, -1)
        }
    }

    fun movePlayerRight() {

        if (getPiece(playerRow, playerCol).isOpenRight()) {
            movePlayer(0, 1)
        }
    }

    private fun movePlayer(row: Int, col: Int) {
        moves++

        playerRow += row
        playerCol += col

        revealAdjacent(playerRow, playerCol)
    }

    private fun blankMap() {
        for (row in 0 until columns) {
            val line = TableRow(context)

            for (col in 0 until rows) {
                line.addView(unexplored.clone(), col)
            }
            addView(line, row)
        }
    }

    private fun start() {

        // First piece will be somewhere in the middle.
        val row = Random.nextInt(1, rows - 1)
        val column = Random.nextInt(1, rows - 1)
        val piece = Fourway(context)

        setPiece(row, column, piece)
        revealAdjacent(row, column)

        playerRow = row
        playerCol = column
    }

    private fun setPiece(row: Int, col: Int, piece: MazePiece) {

        // Don't do anything when we are out of bounds.
        if (!isInBounds(row, col)) {
            return
        }

        // Swap out the piece for something we care about.
        val line = getChildAt(row) as TableRow
        line.removeViewAt(col)
        line.addView(piece.clone(), col)
    }

    // Get the piece that is placed at the the given coordinates.
    // If out of bounds, returns outOfBoundsPiece
    private fun getPiece(row: Int, col: Int): MazePiece {

        // Return outOfBoundsPiece when out of bounds
        if (!isInBounds(row, col)) {
            return outOfBoundsPiece
        }

        val line = getChildAt(row) as TableRow
        return line.getChildAt(col) as MazePiece
    }

    // Reveal all of the pieces that are adjacent to the known piece.
    private fun revealAdjacent(row: Int, col: Int) {
        revealTop(row, col)
        revealBottom(row, col)
        revealLeft(row, col)
        revealRight(row, col)
    }

    // Reveal the piece directly above the current piece.
    private fun revealTop(row: Int, col: Int) {

        // Can't reveal outside of the bounds
        if (!isInBounds(row - 1, col)) {
            return
        }

        // Current piece doesn't allow revealing top
        val knownPiece = getPiece(row, col)
        if (!knownPiece.isOpenTop()) {
            return
        }

        // The top piece is already explored, nothing to reveal
        val piece = getPiece(row - 1, col)
        if (piece.isExplored()) {
            return
        }

        // Get a new piece and place it
        setPiece(row - 1, col, getNewPiece(row - 1, col))
    }

    // Reveal the piece directly below the current piece.
    private fun revealBottom(row: Int, col: Int) {

        // Can't reveal outside of the bounds
        if (!isInBounds(row + 1, col)) {
            return
        }

        // Current piece doesn't allow revealing bottom
        val knownPiece = getPiece(row, col)
        if (!knownPiece.isOpenBottom()) {
            return
        }

        // The top piece is already explored, nothing to reveal
        val piece = getPiece(row + 1, col)
        if (piece.isExplored()) {
            return
        }

        // Get a new piece and place it
        setPiece(row + 1, col, getNewPiece(row + 1, col))
    }

    // Reveal the piece directly left of the current piece.
    private fun revealLeft(row: Int, col: Int) {

        // Can't reveal outside of the bounds
        if (!isInBounds(row, col - 1)) {
            return
        }

        // Current piece doesn't allow revealing left
        val knownPiece = getPiece(row, col)
        if (!knownPiece.isOpenLeft()) {
            return
        }

        // The top piece is already explored, nothing to reveal
        val piece = getPiece(row, col - 1)
        if (piece.isExplored()) {
            return
        }

        // Get a new piece and place it
        setPiece(row, col - 1, getNewPiece(row, col - 1))
    }

    // Reveal the piece directly right of the current piece.
    private fun revealRight(row: Int, col: Int) {

        // Can't reveal outside of the bounds
        if (!isInBounds(row, col + 1)) {
            return
        }

        // Current piece doesn't allow revealing left
        val knownPiece = getPiece(row, col)
        if (!knownPiece.isOpenRight()) {
            return
        }

        // The top piece is already explored, nothing to reveal
        val piece = getPiece(row, col + 1)
        if (piece.isExplored()) {
            return
        }

        // Get a new piece and place it
        setPiece(row, col + 1, getNewPiece(row, col + 1))
    }

    private fun getNewPiece(row: Int, col: Int): MazePiece {
        val validPieces: MutableList<MazePiece> = mutableListOf()

        val mustHaveOpenTop = mustHaveTopOpening(row, col)
        val cannotHaveOpenTop = cantHaveTopOpening(row, col)

        val mustHaveOpenBottom = mustHaveBottomOpening(row, col)
        val cannotHaveOpenBottom = cantHaveBottomOpening(row, col)

        val mustHaveOpenLeft = mustHaveLeftOpening(row, col)
        val cannotHaveOpenLeft = cantHaveLeftOpening(row, col)

        val mustHaveOpenRight = mustHaveRightOpening(row, col)
        val cannotHaveOpenRight = cantHaveRightOpening(row, col)

        val possibleOpenings = numPossibleTilesToFill()

        val neededConnections = exploredConnectionAtUnexplored(row, col)

        for (piece in allMazePieces) {
            if (piece.isOpenTop() && cannotHaveOpenTop || !piece.isOpenTop() && mustHaveOpenTop) {
                continue
            }

            if (piece.isOpenBottom() && cannotHaveOpenBottom || !piece.isOpenBottom() && mustHaveOpenBottom) {
                continue
            }

            if (piece.isOpenLeft() && cannotHaveOpenLeft || !piece.isOpenLeft() && mustHaveOpenLeft) {
                continue
            }

            if (piece.isOpenRight() && cannotHaveOpenRight || !piece.isOpenRight() && mustHaveOpenRight) {
                continue
            }

            val pieceOpenings = piece.numOpenings()

            if(possibleOpenings == 1)
            {
                if(pieceOpenings <= neededConnections) {
                    continue
                }
            }

            validPieces.add(piece)
        }

        // If no valid pieces were found, ditch out.
        if (validPieces.size == 0) {
            goalRow = row
            goalCol = col
            return goalPiece
        }

        // Return a random valid piece.
        val randInt = Random.nextInt(0, validPieces.size)

        return validPieces[randInt]
    }

    // Checks if the piece at the given coordinates must have a top opening
    private fun mustHaveTopOpening(row: Int, col: Int): Boolean {

        // You do not need a top opening if you are at the edge of map
        if (!isInBounds(row - 1, col)) {
            return false
        }

        // If the piece is not explored, no need to force a top
        val piece = getPiece(row - 1, col)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece above is open to the bottom, we are required to have a top opening
        return piece.isOpenBottom()
    }

    // Checks if the piece at the given coordinates can't have a top opening
    private fun cantHaveTopOpening(row: Int, col: Int): Boolean {

        // You can never go out of bounds
        if (!isInBounds(row - 1, col)) {
            return true
        }

        // If the piece is not explored, not need to force no top
        val piece = getPiece(row - 1, col)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece above is not open on the bottom, we are required to not have a top opening
        return !piece.isOpenBottom()
    }

    // Checks if the piece at the given coordinates must have a bottom opening
    private fun mustHaveBottomOpening(row: Int, col: Int): Boolean {

        // You do not need a bottom opening if you are at the edge of map
        if (!isInBounds(row + 1, col)) {
            return false
        }

        // If the piece is not explored, no need to force a bottom
        val piece = getPiece(row + 1, col)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece below is open to the top, we are required to have a bottom opening
        return piece.isOpenTop()
    }

    // Checks if the piece at the given coordinates can't have a bottom opening
    private fun cantHaveBottomOpening(row: Int, col: Int): Boolean {

        // You can never go out of bounds
        if (!isInBounds(row + 1, col)) {
            return true
        }

        // If the piece is not explored, not need to force no bottom
        val piece = getPiece(row + 1, col)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece below is not open on the top, we are required to not have a bottom opening
        return !piece.isOpenTop()
    }

    // Checks if the piece at the given coordinates must have a left opening
    private fun mustHaveLeftOpening(row: Int, col: Int): Boolean {

        // You do not need a left opening if you are at the edge of map
        if (!isInBounds(row, col - 1)) {
            return false
        }

        // If the piece is not explored, no need to force a left
        val piece = getPiece(row, col - 1)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece to the left is open to the right, we are required to have a left opening
        return piece.isOpenRight()
    }

    // Checks if the piece at the given coordinates can't have a left opening
    private fun cantHaveLeftOpening(row: Int, col: Int): Boolean {

        // You can never go out of bounds
        if (!isInBounds(row, col - 1)) {
            return true
        }

        // If the piece is not explored, not need to force no left
        val piece = getPiece(row, col - 1)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece to the left is not open on the right, we are required to not have a left opening
        return !piece.isOpenRight()
    }

    // Checks if the piece at the given coordinates must have a right opening
    private fun mustHaveRightOpening(row: Int, col: Int): Boolean {

        // You do not need a right opening if you are at the edge of map
        if (!isInBounds(row, col + 1)) {
            return false
        }

        // If the piece is not explored, no need to force a right
        val piece = getPiece(row, col + 1)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece to the right is open to the left, we are required to have a right opening
        return piece.isOpenLeft()
    }

    // Checks if the piece at the given coordinates can't have a right opening
    private fun cantHaveRightOpening(row: Int, col: Int): Boolean {

        // You can never go out of bounds
        if (!isInBounds(row, col + 1)) {
            return true
        }

        // If the piece is not explored, not need to force no right
        val piece = getPiece(row, col + 1)
        if (!piece.isExplored()) {
            return false
        }

        // If the piece to the right is not open on the left, we are required to not have a right opening
        return !piece.isOpenLeft()
    }

    private fun isInBounds(row: Int, col: Int): Boolean {
        return row in 0..(rows - 1) && col in 0..(rows - 1)
    }

    private fun numPossibleTilesToFill() : Int {
        var openings = 0

        for(row in 0 until rows)
        {
            for(col in 0 until columns)
            {
                val piece = getPiece(row, col)

                // When we have a unplaced tile look at all adjacent pieces for possible openings
                if(!piece.isExplored())
                {
                    val topPiece = getPiece(row-1, col)
                    val bottomPiece = getPiece(row+1, col)
                    val leftPiece = getPiece(row, col-1)
                    val rightPiece = getPiece(row, col+1)

                    if( (topPiece.isExplored() && topPiece.isOpenBottom()) ||
                        (bottomPiece.isExplored() && bottomPiece.isOpenTop()) ||
                        (leftPiece.isExplored() && leftPiece.isOpenRight()) ||
                        (rightPiece.isExplored() && rightPiece.isOpenLeft()))
                    {
                        openings++
                    }
                }
            }
        }

        return openings
    }

    private fun exploredConnectionAtUnexplored(row: Int, col: Int) : Int {
        var openings = 0

        val piece = getPiece(row, col)

        // Get all of the openings of an unexplored piece
        if(!piece.isExplored())
        {
            val topPiece = getPiece(row-1, col)
            val bottomPiece = getPiece(row+1, col)
            val leftPiece = getPiece(row, col-1)
            val rightPiece = getPiece(row, col+1)

            if(topPiece.isExplored() && topPiece.isOpenBottom())
            {
                openings++
            }

            if(bottomPiece.isExplored() && bottomPiece.isOpenTop())
            {
                openings++
            }

            if(leftPiece.isExplored() && leftPiece.isOpenRight())
            {
                openings++
            }

            if(rightPiece.isExplored() && rightPiece.isOpenLeft())
            {
                openings++
            }
        }

        return openings
    }

}