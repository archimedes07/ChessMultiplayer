package org.chess.core;

import java.io.Serializable;

public abstract class Piece implements Serializable {

    private final Player player;
    private int totalMoves;

    public Piece(Player player){
        this.player = player;
        this.totalMoves = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void incrementMoves(){
        this.totalMoves++;
    }

    public String getType(){
        return getClass().getSimpleName().toLowerCase() + "_" + player.name().toLowerCase();
    }

    public boolean isValidMove(int fromX, int toX, int fromY, int toY, Board board){
        if (toX == -1 || toY == -1){
            return false;
        }

        Piece start = board.getPieces()[fromY][fromX];
        Piece end = board.getPieces()[toY][toX];

        if (start == null){
            return false;
        }

        if (end != null) {
            if (start.getPlayer() == end.getPlayer()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().toLowerCase() + "_" + player.name().toLowerCase();
    }
}
