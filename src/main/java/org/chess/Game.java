package org.chess;

public class Game {

	private int lastFromX, lastFromY, lastToX, lastToY;

	public static void makeMove(int fromX, int toX, int fromY, int toY, int[][] board){

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

		public static boolean isInCheck(int player, int[][] board) {
			int kingX = -1, kingY = -1;
			int kingId = player == Util.PAWN_WHITE_ID ? Util.KING_WHITE_ID : Util.KING_BLACK_ID;

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int piece = board[x][y];
					if (piece == kingId) {
						kingX = x;
						kingY = y;
						break;
					}
				}
			}

			for (int y = 0; y < 8; y++) {
				for (int x = 0; x < 8; x++) {
					int piece = board[x][y];
					if (piece != 0 && Util.getOwner(piece) != player) {
						if (piece.isValidMove(x, kingX, y, kingY, this)) {
							return true;
						}
					}
				}
			}

			return false;
		}

		public void makeMove(int fromX, int toX, int fromY, int toY){
			Piece piece = pieces[fromY][fromX];
			if (isValidCastle(fromX, toX, fromY, toY)){
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
		}

		public void justMakeMove(int fromX, int toX, int fromY, int toY){
			Piece piece = pieces[fromY][fromX];
			pieces[fromY][fromX] = null;
			pieces[toY][toX] = piece;
			currentPlayer = currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
		}
}
