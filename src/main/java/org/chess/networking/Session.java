package org.chess.networking;

import org.chess.core.Board;
import org.chess.core.Player;

import java.util.UUID;

public class Session {

	private Board board;
	private String id;
	private ClientHandler white;
	private ClientHandler black;
	private ClientHandler client1;
	private ClientHandler client2;

	public Session(ClientHandler client1, ClientHandler client2){
		System.out.println("session created");
		this.id = UUID.randomUUID().toString();
		this.board = new Board();
		this.white = client1;
		this.black = client1;
		this.client1 = client1;
		this.client2 = client2;
	}

	public Board getBoard() {
		return board;
	}

	public String getId() {
		return id;
	}

	public ClientHandler getClient1() {
		return client1;
	}

	public ClientHandler getClient2() {
		return client2;
	}

	public boolean makeMove(int fromX, int toX, int fromY, int toY, ClientHandler callingClient){
		if (board.getCurrentPlayer() == Player.WHITE && callingClient == black){
			return false;
		}
		if (board.getCurrentPlayer() == Player.WHITE && callingClient == white){
			return false;
		}
		return board.makeMove(fromX, toX, fromY, toY);
	}

}