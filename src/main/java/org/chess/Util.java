package org.chess;

import java.util.List;

public class Util {

	public static final int PAWN_WHITE_ID = 1;
	public static final int ROOK_WHITE_ID = 2;
	public static final int KNIGHT_WHITE_ID = 3;
	public static final int BISHOP_WHITE_ID = 4;
	public static final int KING_WHITE_ID = 5;
	public static final int QUEEN_WHITE_ID = 6;

	public static final int PAWN_BLACK_ID = 7;
	public static final int ROOK_BLACK_ID = 8;
	public static final int KNIGHT_BLACK_ID = 9;
	public static final int BISHOP_BLACK_ID = 10;
	public static final int KING_BLACK_ID = 11;
	public static final int QUEEN_BLACK_ID = 12;

	public static final List<Integer> WHITE_PIECES = List.of(PAWN_WHITE_ID,ROOK_WHITE_ID,KING_WHITE_ID,BISHOP_WHITE_ID,QUEEN_WHITE_ID);
	public static final List<Integer> BLACK_PIECES = List.of(PAWN_BLACK_ID,ROOK_BLACK_ID,KING_BLACK_ID,BISHOP_BLACK_ID,QUEEN_BLACK_ID);

	public static final int PLAYER_WHITE = 0;
	public static final int PLAYER_BLACK = 1;

	public static final String MATCHMAKING_REQUEST_MESSAGE = "matchmaking";
	public static final String GET_BOARD_MESSAGE = "getboard";
	public static final String GAME_START_MESSAGE = "s";
	public static final String MOVE_REQUEST_PATTERN = "m%d%d%d%d";

	public static int getOwner(int pieceId){
		if (WHITE_PIECES.contains(pieceId)){
			return PLAYER_WHITE;
		}
		if (BLACK_PIECES.contains(pieceId)){
			return PLAYER_BLACK;
		}
		return -1;
	}

}
