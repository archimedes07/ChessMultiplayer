package org.chess.core;

import org.chess.core.pieces.*;

import java.io.Serializable;

public class Board implements Serializable {

    private Piece[][] pieces;
    private Player currentPlayer;
    private int lastFromX, lastFromY, lastToX, lastToY;

    public Board(){
        initializeBoard();
    }

    public int getLastFromX() {
        return lastFromX;
    }

    public int getLastFromY() {
        return lastFromY;
    }

    public int getLastToX() {
        return lastToX;
    }

    public int getLastToY() {
        return lastToY;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void initializeBoard(){
        currentPlayer = Player.WHITE;
        pieces = new Piece[8][8];

        for (int i = 0; i<8; i++){
            pieces[1][i] = new Pawn(Player.BLACK);
            pieces[6][i] = new Pawn(Player.WHITE);
        }

        pieces[0][0] = new Rook(Player.BLACK);
        pieces[0][1] = new Knight(Player.BLACK);
        pieces[0][2] = new Bishop(Player.BLACK);
        pieces[0][3] = new Queen(Player.BLACK);
        pieces[0][4] = new King(Player.BLACK);
        pieces[0][5] = new Bishop(Player.BLACK);
        pieces[0][6] = new Knight(Player.BLACK);
        pieces[0][7] = new Rook(Player.BLACK);

        pieces[7][0] = new Rook(Player.WHITE);
        pieces[7][1] = new Knight(Player.WHITE);
        pieces[7][2] = new Bishop(Player.WHITE);
        pieces[7][3] = new Queen(Player.WHITE);
        pieces[7][4] = new King(Player.WHITE);
        pieces[7][5] = new Bishop(Player.WHITE);
        pieces[7][6] = new Knight(Player.WHITE);
        pieces[7][7] = new Rook(Player.WHITE);
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public Piece getPiece(int x, int y){
        return pieces[y][x];
    }

    public boolean isCheckmate() {
        if (!isInCheck(currentPlayer)) {
            return false;
        }

        for (int fromY = 0; fromY < 8; fromY++) {
            for (int fromX = 0; fromX < 8; fromX++) {
                Piece piece = getPiece(fromX, fromY);
                if (piece != null && piece.getPlayer() == currentPlayer) {
                    for (int toY = 0; toY < 8; toY++) {
                        for (int toX = 0; toX < 8; toX++) {
                            if (isValidMove(fromX, toX, fromY, toY)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }


    public boolean isValidMove(int fromX, int toX, int fromY, int toY){
        Piece piece = pieces[fromY][fromX];

        if (piece == null){
            return false;
        }

        if (piece.getPlayer() != currentPlayer){
            return false;
        }

        if (createsSelfCheck(fromX, toX, fromY, toY, currentPlayer)){
            return false;
        }

        return piece.isValidMove(fromX, toX, fromY, toY, this);
    }

    public boolean isValidCastle(int fromX, int toX, int fromY, int toY){
        Piece piece1 = pieces[fromY][fromX];
        Piece piece2 = pieces[toY][toX];

        if (piece1 == null || piece2 == null){
            return false;
        }
        if (piece1.getPlayer() != piece2.getPlayer()){
            return false;
        }
        if (piece1.getTotalMoves() != 0 || piece2.getTotalMoves() != 0){
            return false;
        }

        boolean longCastle = (fromX == 0 || toX == 0);
        int[][] inBetweenFields;

        if (longCastle){
            if (currentPlayer == Player.WHITE){
                inBetweenFields = new int[][] {{1,7}, {2,7}, {3,7}};
            }
            else{
                inBetweenFields = new int[][] {{1,0}, {2,0}, {3,0}};
            }
        }
        else{
            if (currentPlayer == Player.WHITE){
                inBetweenFields = new int[][] {{5,7}, {6,7}};
            }
            else{
                inBetweenFields = new int[][] {{5,0}, {6,0}};
            }
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPiece(x, y);
                if (piece != null && piece.getPlayer() != currentPlayer) {
                    for (int[] field : inBetweenFields) {
                        if (piece.isValidMove(x, field[0], y, field[1], this)) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int[] field : inBetweenFields) {
            Piece piece = getPiece(field[0], field[1]);
            if (piece!=null && piece.getPlayer() == currentPlayer) {
                return false;
            }
        }

        return (piece1 instanceof King && piece2 instanceof Rook) || (piece2 instanceof King && piece1 instanceof Rook);
    }

    public boolean createsSelfCheck(int fromX, int toX, int fromY, int toY, Player player){
        Board simulatedBoard = this.copy();
        simulatedBoard.justMakeMove(fromX, toX, fromY, toY);
        return simulatedBoard.isInCheck(player);
    }

    public Board copy() {
        Board copy = new Board();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                copy.pieces[y][x] = null;
                copy.pieces[y][x] = pieces[y][x];
            }
        }
        return copy;
    }

    public boolean isInCheck(Player player) {
        int kingX = -1, kingY = -1;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPiece(x, y);
                if (piece instanceof King && piece.getPlayer() == player) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
        }

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPiece(x, y);
                if (piece != null && piece.getPlayer() != player) {
                    if (piece.isValidMove(x, kingX, y, kingY, this)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean makeMove(int fromX, int toX, int fromY, int toY){
        boolean madeMove = false;

        Piece piece = pieces[fromY][fromX];
        if (isValidCastle(fromX, toX, fromY, toY)){
            madeMove = true;
            Piece piece1 = pieces[fromY][fromX];
            Piece piece2 = pieces[toY][toX];

            Rook rook;
            King king;

            if (piece1 instanceof Rook){
                rook = (Rook) piece1;
                king = (King) piece2;
            }
            else{
                rook = (Rook) piece2;
                king = (King) piece1;
            }

            // if we castle with the rook on the left side
            boolean longCastle = (fromX == 0 || toX == 0);

            pieces[fromY][fromX] = null;
            pieces[toY][toX] = null;

            if (longCastle){
                if (currentPlayer == Player.WHITE){
                    pieces[7][2] = king;
                    pieces[7][3] = rook;
                }
                else{
                    pieces[0][2] = king;
                    pieces[0][3] = rook;
                }
            }
            else{
                if (currentPlayer == Player.WHITE){
                    pieces[7][6] = king;
                    pieces[7][5] = rook;
                }
                else{
                    pieces[0][6] = king;
                    pieces[0][5] = rook;
                }
            }
            piece1.incrementMoves();
            piece2.incrementMoves();
            currentPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        }
        else if (isValidMove(fromX, toX, fromY, toY)){
            madeMove = true;
            //en passant
            if (piece instanceof Pawn && Math.abs(fromX - toX) == 1 && getPiece(toX, toY) == null) {
                int enPassantPawnY = (piece.getPlayer() == Player.WHITE) ? toY + 1 : toY - 1;
                pieces[enPassantPawnY][toX] = null;
            }

            pieces[fromY][fromX] = null;
            pieces[toY][toX] = piece;

            if (piece instanceof Pawn){
                if (toY == 0 || toY == 7){
                    pieces[toY][toX] = new Queen(currentPlayer);
                }
            }

            piece.incrementMoves();
            lastFromX = fromX;
            lastFromY = fromY;
            lastToX = toX;
            lastToY = toY;

            currentPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        }

        if(isCheckmate()){
            initializeBoard();
        }

        return madeMove;
    }

    public void justMakeMove(int fromX, int toX, int fromY, int toY){
        Piece piece = pieces[fromY][fromX];
        pieces[fromY][fromX] = null;
        pieces[toY][toX] = piece;
        currentPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    private char getSymbol(Piece piece) {
        if (piece instanceof King) return piece.getPlayer() == Player.WHITE ? 'K' : 'k';
        if (piece instanceof Queen) return piece.getPlayer() == Player.WHITE ? 'Q' : 'q';
        if (piece instanceof Rook) return piece.getPlayer() == Player.WHITE ? 'R' : 'r';
        if (piece instanceof Bishop) return piece.getPlayer() == Player.WHITE ? 'B' : 'b';
        if (piece instanceof Knight) return piece.getPlayer() == Player.WHITE ? 'N' : 'n';
        if (piece instanceof Pawn) return piece.getPlayer() == Player.WHITE ? 'P' : 'p';
        return '?'; // Falls eine unbekannte Figur vorkommt
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n"); // Spaltenbeschriftung
        sb.append("  ----------------\n");

        for (int y = 0; y < 8; y++) {
            sb.append((8 - y)).append("|"); // Zeilenbeschriftung

            for (int x = 0; x < 8; x++) {
                Piece piece = pieces[y][x];
                if (piece == null) {
                    sb.append(" ."); // Leeres Feld
                } else {
                    sb.append(" ").append(getSymbol(piece));
                }
            }
            sb.append(" |\n");
        }

        sb.append("  ----------------\n");
        return sb.toString();
    }
}
