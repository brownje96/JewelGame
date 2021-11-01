package game;

import game.jewels.Dummy;
import game.jewels.Jewel;

import javax.swing.*;
import java.awt.*;

public class Window
    extends JFrame {

    private Game myGame;

    public Window() {
        super("Jewel Game - Score: ");
        setSize(450, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void updateGame(Game x) {
        myGame = x;
        setLayout(new GridLayout(myGame.getGameSize().width, myGame.getGameSize().height));
        updateBoard();
    }

    public void updateBoard() {
        setTitle("Jewel Game - Score: " + myGame.getScore());
        getContentPane().removeAll();
        getContentPane().repaint();
        Jewel[][] board = myGame.getBoard();
        for(int x = 0; x < 8; x++) {
            for(int y = 0; y < 8; y++) {
                if(board[x][y] == null) add(new Dummy());   // should only happen in the IDE.
                else add(board[x][y]);
            }
        }
        revalidate();
        repaint();
    }
}