package org.chess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Server {

	private final List<ClientHandler> clientHandlers;
	private final Map<String, Session> sessions;
	private final Map<ClientHandler, String> clientSessionMap;
	private final Queue<ClientHandler> matchmakingQueue;
	private final ServerSocket serverSocket;

	public Server() throws IOException {
		this.serverSocket = new ServerSocket(7331);
		this.clientHandlers = new ArrayList<>();
		this.clientSessionMap = new HashMap<>();
		this.sessions = new HashMap<>();
		this.matchmakingQueue = new LinkedList<>();

		while (true) {
			Socket socket = this.serverSocket.accept();
			ClientHandler clientHandler = new ClientHandler(this, socket);
			new Thread(clientHandler).start();
			clientHandlers.add(clientHandler);
		}
	}

	public void onMatchmaking(ClientHandler callingClient) throws IOException {
		if (!matchmakingQueue.contains(callingClient)) {
			matchmakingQueue.add(callingClient);
		}

		if (matchmakingQueue.size() >= 2){
			ClientHandler client1 = matchmakingQueue.poll();
			ClientHandler client2 = matchmakingQueue.poll();

			if (client1 != null && client2 != null) {
				Session session = new Session();
				sessions.put(session.getId(), session);
				clientSessionMap.put(client1, session.getId());
				clientSessionMap.put(client2, session.getId());
				client1.getOos().writeObject(Util.GAME_START_MESSAGE);
				client2.getOos().flush();
				client1.getOos().writeObject(session.getId());
				client2.getOos().flush();

				client2.getOos().writeObject(Util.GAME_START_MESSAGE);
				client2.getOos().flush();
				client2.getOos().writeObject(session.getId());
				client2.getOos().flush();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server();
	}
}
