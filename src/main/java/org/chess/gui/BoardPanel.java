package org.chess.gui;

import org.chess.core.Board;
import org.chess.core.Piece;
import org.chess.networking.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardPanel extends JPanel implements MouseListener {

    private static final Color WHITE_SQUARE_COLOR = new Color(235, 236 ,208);
    private static final Color BLACK_SQUARE_COLOR = new Color(115, 149 ,82);

    private final Map<String, Image> imageCache = new HashMap<>();
    private Board board;
    private int selectedX, selectedY;
    private Client client;

    public BoardPanel(Client client){
        this.selectedX = -1;
        this.selectedY = -1;
        this.client = client;
        addMouseListener(this);
        setPreferredSize(new Dimension(700, 700));
        loadImages();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        if (board != null) {
            System.out.println("Drawing");
            drawSquares(g);
            drawPieces(g);
        }
        else{
            System.out.println("not drawing");
        }
    }

    public Image loadImage(String path){
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadImages() {
        imageCache.put("king_white", loadImage("images/king_white.png"));
        imageCache.put("queen_white", loadImage("images/queen_white.png"));
        imageCache.put("rook_white", loadImage("images/rook_white.png"));
        imageCache.put("bishop_white", loadImage("images/bishop_white.png"));
        imageCache.put("knight_white", loadImage("images/knight_white.png"));
        imageCache.put("pawn_white", loadImage("images/pawn_white.png"));

        imageCache.put("king_black", loadImage("images/king_black.png"));
        imageCache.put("queen_black", loadImage("images/queen_black.png"));
        imageCache.put("rook_black", loadImage("images/rook_black.png"));
        imageCache.put("bishop_black", loadImage("images/bishop_black.png"));
        imageCache.put("knight_black", loadImage("images/knight_black.png"));
        imageCache.put("pawn_black", loadImage("images/pawn_black.png"));
    }

    public int getSquareWidth(){
        return getWidth()/8;
    }

    public int getSquareHeight(){
        return getHeight()/8;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void drawLines(Graphics g){
        g.setColor(Color.BLACK);
        int width = getSquareWidth();
        int height = getSquareHeight();
        for (int i = 1; i<=8; i++){
            g.drawLine(i*width, 0, i*width, getHeight());
        }
        for (int i = 1; i<=8; i++){
            g.drawLine(0, i*height, getWidth(), i*height);
        }
    }

    public void drawSquares(Graphics g){
        int width = getSquareWidth();
        int height = getSquareHeight();
        for (int y = 0; y<8; y++){
            for (int x = 0; x<8; x++){
                Color color = (x + y) % 2 == 0 ? WHITE_SQUARE_COLOR : BLACK_SQUARE_COLOR;
                g.setColor(color);
                if (x == selectedX && y == selectedY){
                    g.setColor(Color.RED);
                }
                g.fillRect(x*width, y*height, width, height);
            }
        }
    }

    public void drawPieces(Graphics g){
        int width = getSquareWidth();
        int height = getSquareHeight();
        for (int y = 0; y<8; y++){
            for (int x = 0; x<8; x++){
                Piece piece = board.getPieces()[y][x];
                if (piece != null){
                    String type = piece.getType();
                    Image image = imageCache.get(type);
                    g.drawImage(image, x*width, y*height, width, height, null);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int squareX = x / getSquareWidth();
        int squareY = y / getSquareHeight();

        if (selectedX == -1 || selectedY == -1){
            selectedX = squareX;
            selectedY = squareY;
        }
        else{
            client.makeMove(selectedX, squareX, selectedY, squareY);
            selectedX = -1;
            selectedY = -1;
        }

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
