package org.chess.core.pieces;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.core.Player;

public class Pawn extends Piece {

    public Pawn(Player player) {
        super(player);
    }

    @Override
    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board) {
        if (!super.isValidMove(fromX, toX, fromY, toY, board)){
            return false;
        }

        int direction = (this.getPlayer() == Player.WHITE) ? -1 : 1;
        int startRow = (this.getPlayer() == Player.WHITE) ? 6 : 1;
        Piece end = board.getPieces()[toY][toX];

        if (toX == fromX && toY == fromY + direction && end == null) {
            return true;
        }

        if (toX == fromX && toY == fromY + 2 * direction && fromY == startRow &&
                end == null && board.getPieces()[fromY + direction][toX] == null) {
            return true;
        }

        if (Math.abs(toX - fromX) == 1 && toY == fromY + direction && end != null) {
            return true;
        }

        if (Math.abs(toX - fromX) == 1 && toY == fromY + direction && end == null) {
            // Prüfe, ob der letzte Zug ein zweier-Schritt eines gegnerischen Bauern war
            if (board.getLastToY() == fromY && board.getLastFromY() == fromY + 2 * direction &&
                    board.getLastToX() == toX) {
                return true;
            }
        }

        return false;
    }

}
