package org.chess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{

	private final Server server;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;

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

	@Override
	public void run() {
		try {
			while (true) {
				String message = (String) ois.readObject();
				System.out.println("Received: " + message);
				switch (message){
					case Util.MATCHMAKING_REQUEST_MESSAGE:
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