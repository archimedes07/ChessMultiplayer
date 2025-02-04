package org.chess.core.pieces;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.core.Player;

public class Bishop extends Piece {

    public Bishop(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board) {
        if (!super.isValidMove(fromX, toX, fromY, toY, board)) {
            return false;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);

        if (dx != dy) {
            return false;
        }

        int stepX = (toX > fromX) ? 1 : -1;
        int stepY = (toY > fromY) ? 1 : -1;

        int x = fromX + stepX;
        int y = fromY + stepY;

        while (x != toX && y != toY) {
            if (board.getPiece(x, y) != null) {
                return false;
            }
            x += stepX;
            y += stepY;
        }

        return true;
    }

}
