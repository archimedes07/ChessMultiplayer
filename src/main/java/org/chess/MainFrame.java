package org.chess;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {

    public LobbyPanel lobbyPanel;

    public MainFrame(Client client){
        setTitle("Chess");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(lobbyPanel = new LobbyPanel(client), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
