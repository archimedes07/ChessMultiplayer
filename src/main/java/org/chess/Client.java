package org.chess;

import org.chess.core.Board;
import org.chess.gui.MainFrame;
import org.chess.networking.NetUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	private final Socket socket;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;
	private final MainFrame mainFrame;

	public Client() throws IOException {
		this.socket = new Socket("localhost", 7331);
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
		this.mainFrame = new MainFrame(this);

		Thread receiverThread = new Thread(() ->{
			try {
				while (true) {
					Object obj = ois.readObject();

					if (obj instanceof Board){
						Board board = (Board) obj;
						System.out.println(board);
						mainFrame.updateBoard(board);
						mainFrame.showBoard();
					}
				}
			}catch (IOException | ClassNotFoundException e){
				System.out.println("Error in receiver thread: " + e.getMessage());
			}
		});

		receiverThread.start();
	}

	public void makeMove(int fromX, int toX, int fromY, int toY){
		try {
			oos.writeObject(String.format(NetUtils.MOVE_REQUEST_FORM, fromX, toX, fromY, toY));
			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int[][] getBoardFromServer(){
		try {
			oos.writeObject(NetUtils.UPDATE_BOARD_MESSAGE);
			oos.flush();
			return (int[][]) ois.readObject();
		}catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void requestMatchmaking(){
		try {
			oos.writeObject(NetUtils.MATCHMAKING_REQUEST_MESSAGE);
			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client client = new Client();
	}
}
