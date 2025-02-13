package org.chess.gui;

import org.chess.networking.Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

public class LobbyPanel extends JPanel {

	public LobbyPanel(Client client){
		setPreferredSize(new Dimension(700, 700));
		JButton playButton = new JButton("Play");
		playButton.addActionListener(e -> client.requestMatchmaking());
		setLayout(new BorderLayout());
		add(playButton, BorderLayout.CENTER);
	}

}
