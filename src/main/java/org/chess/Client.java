package org.chess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	private final Socket socket;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;
	private final MainFrame mainFrame;
	private String currentSessionId;

	public Client() throws IOException {
		this.socket = new Socket("localhost", 7331);
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
		this.mainFrame = new MainFrame(this);

		Thread receiverThread = new Thread(() ->{
			try {
				while (true) {
					String message = (String) ois.readObject();
					if (message.equals(Util.GAME_START_MESSAGE)){
						currentSessionId = (String) ois.readObject();
						System.out.println("new Session: " + currentSessionId);
					}
				}
			}catch (IOException | ClassNotFoundException e){
				System.out.println("Error in receiver thread: " + e.getMessage());
			}
		});

		receiverThread.start();
	}

	public void makeMove(int fromX, int toX, int fromY, int toY, int sessionId){
		try {
			oos.writeObject(String.format(Util.MOVE_REQUEST_PATTERN, fromX, toX, fromY, toY));
			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int[][] getBoardFromServer(){
		try {
			oos.writeObject(Util.GET_BOARD_MESSAGE);
			oos.flush();
			return (int[][]) ois.readObject();
		}catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void requestMatchmaking(){
		try {
			oos.writeObject(Util.MATCHMAKING_REQUEST_MESSAGE);
			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client client = new Client();
	}
}
