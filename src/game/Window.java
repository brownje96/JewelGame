package game;

import game.components.WebPane;
import game.jewels.Dummy;
import game.jewels.Jewel;
import game.meta.Metadata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window
    extends JFrame {

    // Window components
    JMenuBar mnuBar = new JMenuBar();
    // menus
    JMenu mnuFile = new JMenu("File");
    JMenu mnuHelp = new JMenu("Help");
    JMenu mnuScore = new JMenu("0");

    // menu items for File
    JMenuItem itmNew = new JMenuItem("New Game");
    JMenuItem itmCustom = new JMenuItem("Custom Game");
    JMenuItem itmExit = new JMenuItem("Exit");

    // menu items for Help
    JMenuItem itmHelp = new JMenuItem("How to Play");
    JMenuItem itmAbout = new JMenuItem("About " + Metadata.APP_NAME);

    // game data
    private Game myGame;

    // constructor
    public Window() {
        super(Metadata.APP_NAME);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        prepareMenu();
        add(new JLabel(String.format(Utility.readEntireFileAsStr(getClass().getResourceAsStream("meta/splash.html")), Metadata.APP_NAME), SwingConstants.CENTER));
    }

    private void prepareMenu() {
        setJMenuBar(mnuBar);

        // File Menu
        mnuBar.add(mnuFile);
        mnuFile.add(itmNew);
        itmNew.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                Global.currentGame = new Game(8, 8);
                updateGame(Global.currentGame);
            }
        });
        mnuFile.add(itmCustom);
        itmCustom.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                SpinnerNumberModel  snmX = new SpinnerNumberModel(8, 4, 100, 1),
                                    snmY = new SpinnerNumberModel(8, 4, 100, 1);
                JSpinner x = new JSpinner(snmX), y = new JSpinner(snmY);
                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("X: "));
                panel.add(x);
                panel.add(new JLabel("Y: "));
                panel.add(y);
                JOptionPane.showMessageDialog(Global.mainWindow, panel, "Define Size", JOptionPane.QUESTION_MESSAGE);
                Global.currentGame = new Game((Integer) x.getValue(), (Integer) y.getValue());
                updateGame(Global.currentGame);
            }
        });
        mnuFile.add(itmExit);
        itmExit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {dispose();}
        });

        // Help Menu
        mnuBar.add(mnuHelp);
        mnuHelp.add(itmHelp);
        itmHelp.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Global.mainWindow,
                    new WebPane(new Dimension(640,240), getClass().getResourceAsStream("meta/help.html")),
                    "How to Play",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        mnuHelp.add(itmAbout);
        itmAbout.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                    Global.mainWindow,
                    new WebPane(
                        new Dimension(640,240),
                        getClass().getResourceAsStream("meta/credits.html"),
                        Metadata.APP_NAME,
                        Metadata.APP_AUTH,
                        Metadata.APP_VERS
                    ),
                    itmAbout.getText(),
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // Score Menu
        mnuBar.add(Box.createHorizontalGlue());
        mnuBar.add(mnuScore);
        mnuScore.setEnabled(false);
    }

    public void updateGame(Game x) {
        myGame = x;
        setLayout(new GridLayout(myGame.getGameSize().height, myGame.getGameSize().width));
        updateBoard();
    }

    public void updateBoard() {
        mnuScore.setText(Integer.toString(myGame.getScore()));
        getContentPane().removeAll();
        getContentPane().repaint();
        Jewel[][] board = myGame.getBoard();
        for(int y = 0; y < myGame.getGameSize().height; y++) {
            for(int x = 0; x < myGame.getGameSize().width; x++) {
                if(board[y][x] == null) add(new Dummy());   // should only happen in the IDE.
                else add(board[y][x]);
            }
        }
        revalidate();
        repaint();
    }

    public Game getGame() { return myGame; }
}