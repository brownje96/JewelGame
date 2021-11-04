package game.ui.windows;

import game.*;
import game.jewels.Dummy;
import game.jewels.Jewel;
import game.meta.Metadata;
import game.ui.actions.AboutViewer;
import game.ui.actions.GameConfigurator;
import game.ui.actions.HelpViewer;

import javax.swing.*;
import java.awt.*;

public class Window
    extends JFrame {

    // Window components
    final JMenuBar mnuBar = new JMenuBar();
    // menus
    final JMenu mnuFile = new JMenu("File"),
                mnuHelp = new JMenu("Help"),
                mnuScore = new JMenu("0");

    // menu items for File
    final JMenuItem itmNew = new JMenuItem("New Game"),
                    itmExit = new JMenuItem("Exit");

    // menu items for Help
    final JMenuItem itmHelp = new JMenuItem("How to Play"),
                    itmAbout = new JMenuItem("About " + Metadata.APP_NAME);

    // game controls
    private JPanel boardPanel;
    private final JProgressBar timerBar = new JProgressBar();

    private Thread timerThread;

    // game data
    private Board myBoard;

    // constructor
    public Window() {
        super(Metadata.APP_NAME);
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        prepareMenu();
        add(new JLabel(String.format(Utility.readEntireFileAsStr(getClass().getResourceAsStream("/game/meta/splash.html")), Metadata.APP_NAME), SwingConstants.CENTER));
    }

    private void prepareMenu() {
        setJMenuBar(mnuBar);

        // File Menu
        mnuBar.add(mnuFile);
        mnuFile.add(itmNew);
        itmNew.addActionListener(new GameConfigurator());
        mnuFile.add(itmExit);
        itmExit.addActionListener(e -> dispose());

        // Help Menu
        mnuBar.add(mnuHelp);
        mnuHelp.add(itmHelp);
        itmHelp.addActionListener(new HelpViewer());
        mnuHelp.add(itmAbout);
        itmAbout.addActionListener(new AboutViewer());

        // Score Menu
        mnuBar.add(Box.createHorizontalGlue());
        mnuBar.add(mnuScore);
        mnuScore.setEnabled(false);
    }

    public void updateGame(Board x, Mode m, int seconds) {
        if(timerThread != null) timerThread.interrupt();
        getContentPane().removeAll();
        getContentPane().repaint();
        myBoard = x;
        boardPanel = new JPanel(new GridLayout(myBoard.getGameSize().height, myBoard.getGameSize().width));
        updateBoard();
        if(m == Mode.TIMED) {
            add(timerBar, BorderLayout.SOUTH);
            timerThread = new Thread(new PlayTimer(timerBar, seconds), "Timed Game Thread");
            timerThread.start();
        }
    }

    public void updateBoard() {
        mnuScore.setText(Integer.toString(myBoard.getScore()));
        boardPanel.removeAll();
        add(boardPanel, BorderLayout.CENTER);
        Jewel[][] board = myBoard.getArray();
        for(int y = 0; y < myBoard.getGameSize().height; y++) {
            for(int x = 0; x < myBoard.getGameSize().width; x++) {
                if(board[y][x] == null) boardPanel.add(new Dummy());   // should only happen in the IDE.
                else boardPanel.add(board[y][x]);
            }
        }
        revalidate();
        repaint();
    }

    public void lockBoard() {
        for(int y = 0; y < myBoard.getGameSize().height; y++)
            for(int x = 0; x < myBoard.getGameSize().width; x++)
                myBoard.getArray()[y][x].setEnabled(false);
    }

    public Board getGame() { return myBoard; }
}