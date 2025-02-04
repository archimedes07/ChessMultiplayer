package org.chess.core.pieces;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.core.Player;

public class Queen extends Piece {

    public Queen(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board) {
        if (!super.isValidMove(fromX, toX, fromY, toY, board)) {
            return false;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);

        boolean isRookMove = (dx == 0 || dy == 0);
        boolean isBishopMove = (dx == dy);

        if (!isRookMove && !isBishopMove) {
            return false;
        }

        int stepX = Integer.compare(toX, fromX);
        int stepY = Integer.compare(toY, fromY);

        int x = fromX + stepX;
        int y = fromY + stepY;

        while (x != toX || y != toY) {
            if (board.getPiece(x, y) != null) {
                return false;
            }
            x += stepX;
            y += stepY;
        }

        return true;
    }

}
