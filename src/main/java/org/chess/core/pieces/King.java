package org.chess.core.pieces;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.core.Player;

public class King extends Piece {

    public King(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board) {
        if (!super.isValidMove(fromX, toX, fromY, toY, board)) {
            return false;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);

        return dx <= 1 && dy <= 1;
    }

}
