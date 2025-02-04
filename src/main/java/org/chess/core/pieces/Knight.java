package org.chess.core.pieces;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.core.Player;

public class Knight extends Piece {

    public Knight(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board) {
        if (!super.isValidMove(fromX, toX, fromY, toY, board)){
            return false;
        }

        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }
}
