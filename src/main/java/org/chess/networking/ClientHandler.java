package org.chess.networking;

import org.chess.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

	private final Server server;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;
	private String currentSessionId;

	public ClientHandler(Server server, Socket socket) throws IOException {
		this.server = server;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.ois = new ObjectInputStream(socket.getInputStream());
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public String getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String message = (String) ois.readObject();

				if (message.matches(NetUtils.MOVE_REQUEST_PATTERN)){
					String numbers = message.substring(1);
					char[] digits = numbers.toCharArray();
					int fromX = Character.getNumericValue(digits[0]);
					int toX = Character.getNumericValue(digits[1]);
					int fromY = Character.getNumericValue(digits[2]);
					int toY = Character.getNumericValue(digits[3]);
					server.makeMove(fromX, toX, fromY, toY, currentSessionId, this);
					continue;
				}

				switch (message){
					case NetUtils.MATCHMAKING_REQUEST_MESSAGE:
						server.onMatchmaking(this);
						break;
				}
			}
		}
		catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}