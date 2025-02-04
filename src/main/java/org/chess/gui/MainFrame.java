package org.chess.gui;

import org.chess.core.Board;
import org.chess.networking.Client;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private LobbyPanel lobbyPanel;
    private BoardPanel boardPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MainFrame(Client client) {
        setTitle("Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        lobbyPanel = new LobbyPanel(client);
        boardPanel = new BoardPanel(client);

        cardPanel.add(lobbyPanel, "LOBBY");
        cardPanel.add(boardPanel, "BOARD");

        add(cardPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateBoard(Board board){
        boardPanel.setBoard(board);
        boardPanel.repaint();
    }

    public void showBoard() {
        cardLayout.show(cardPanel, "BOARD");
    }

    public void showLobby() {
        cardLayout.show(cardPanel, "LOBBY");
    }
}
