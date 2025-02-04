package org.chess;

import java.util.UUID;

public class Session {

	private int[][] board;
	private String id;
	private int currentPlayer;

	public Session(){
		this.id = UUID.randomUUID().toString();
		this.board = createBoard();
		this.currentPlayer = Util.PLAYER_WHITE;
	}

	public int[][] getBoard() {
		return board;
	}

	public String getId() {
		return id;
	}

	public int[][] createBoard() {
		int[][] board = new int[8][8];

		for (int i = 0; i < 8; i++) {
			board[1][i] = Util.PAWN_BLACK_ID;
			board[6][i] = Util.PAWN_WHITE_ID;
		}

		board[0][0] = Util.ROOK_BLACK_ID;
		board[0][1] = Util.KNIGHT_BLACK_ID;
		board[0][2] = Util.BISHOP_BLACK_ID;
		board[0][3] = Util.QUEEN_BLACK_ID;
		board[0][4] = Util.KING_BLACK_ID;
		board[0][5] = Util.BISHOP_BLACK_ID;
		board[0][6] = Util.KNIGHT_BLACK_ID;
		board[0][7] = Util.ROOK_BLACK_ID;

		board[7][0] = Util.ROOK_WHITE_ID;
		board[7][1] = Util.KNIGHT_WHITE_ID;
		board[7][2] = Util.BISHOP_WHITE_ID;
		board[7][3] = Util.QUEEN_WHITE_ID;
		board[7][4] = Util.KING_WHITE_ID;
		board[7][5] = Util.BISHOP_WHITE_ID;
		board[7][6] = Util.KNIGHT_WHITE_ID;
		board[7][7] = Util.ROOK_WHITE_ID;

		return board;
	}

}