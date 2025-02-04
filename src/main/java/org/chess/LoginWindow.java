package org.chess;

import org.chess.networking.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginWindow extends JFrame {

    private final JTextField ipTxtField;
    private final JTextField portTxtField;

    public LoginWindow(){
        setTitle("Login");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));
        ipTxtField = new JTextField();
        portTxtField = new JTextField();
        add(ipTxtField);
        add(portTxtField);
        JButton loginButton = new JButton("connect");
        loginButton.addActionListener(
                e -> {
                    try {
                        new Client(ipTxtField.getText(), Integer.parseInt(portTxtField.getText()));
                        this.dispose();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
        );
        add(loginButton);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}
