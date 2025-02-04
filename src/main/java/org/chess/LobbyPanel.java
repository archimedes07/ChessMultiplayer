package org.chess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

public class LobbyPanel extends JPanel {

	private final Client client;

	public LobbyPanel(Client client){
		this.client = client;
		setPreferredSize(new Dimension(700, 700));
		JButton playButton = new JButton("Play");
		playButton.addActionListener(
				e -> {
					client.requestMatchmaking();
				}
		);
		setLayout(new BorderLayout());
		add(playButton, BorderLayout.CENTER);
	}

}
