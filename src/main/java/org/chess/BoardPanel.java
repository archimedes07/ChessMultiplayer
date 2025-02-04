package org.chess;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener {

    private static final Color WHITE_SQUARE_COLOR = new Color(235, 236 ,208);
    private static final Color BLACK_SQUARE_COLOR = new Color(115, 149 ,82);

    private final Map<Integer, Image> imageCache = new HashMap<>();
    private final int[][] board;
    private int selectedX, selectedY;

    public BoardPanel(int[][] board, Client client){
        this.board = board;
        this.selectedX = -1;
        this.selectedY = -1;
        addMouseListener(this);
        setPreferredSize(new Dimension(700, 700));
        loadImages();
        requestFocus();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        drawSquares(g);
        drawPieces(g);
    }

    public Image loadImage(String path){
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadImages() {
        imageCache.put(Util.KING_WHITE_ID, loadImage("images/king_white.png"));
        imageCache.put(Util.QUEEN_WHITE_ID, loadImage("images/queen_white.png"));
        imageCache.put(Util.ROOK_WHITE_ID, loadImage("images/rook_white.png"));
        imageCache.put(Util.BISHOP_WHITE_ID, loadImage("images/bishop_white.png"));
        imageCache.put(Util.KNIGHT_WHITE_ID, loadImage("images/knight_white.png"));
        imageCache.put(Util.PAWN_WHITE_ID, loadImage("images/pawn_white.png"));

        imageCache.put(Util.KING_BLACK_ID, loadImage("images/king_black.png"));
        imageCache.put(Util.QUEEN_BLACK_ID, loadImage("images/queen_black.png"));
        imageCache.put(Util.ROOK_BLACK_ID, loadImage("images/rook_black.png"));
        imageCache.put(Util.BISHOP_BLACK_ID, loadImage("images/bishop_black.png"));
        imageCache.put(Util.KNIGHT_BLACK_ID, loadImage("images/knight_black.png"));
        imageCache.put(Util.PAWN_BLACK_ID, loadImage("images/pawn_black.png"));

    }

    public int getSquareWidth(){
        return getWidth()/8;
    }

    public int getSquareHeight(){
        return getHeight()/8;
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
                int piece = board[y][x];
                if (piece != 0){
                    Image image = imageCache.get(piece);
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
            Game.makeMove(selectedX, squareX, selectedY, squareY, board);
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
