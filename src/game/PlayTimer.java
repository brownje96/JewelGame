package game;

import javax.swing.*;

public class PlayTimer
    implements Runnable {

    private final JProgressBar bar;
    private final int initialTime;
    private int remainingTime;

    public PlayTimer(JProgressBar myBar, int seconds) {
        bar = myBar;
        initialTime = seconds;
        remainingTime = seconds;
    }

    @Override public void run() {
        // prep
        bar.setValue(100);
        // timing
        while(remainingTime > 0) {
            Utility.waitSeconds(1);
            remainingTime--;
            bar.setValue((int) ((((double) remainingTime) / ((double)initialTime)) * 100));
            bar.setString(Integer.toString(remainingTime)); //todo: time formatting
        }
        Global.currentBoard.deSelectAllJewels();
        Global.mainWindow.lockBoard();
        JOptionPane.showMessageDialog(Global.mainWindow, "Your score is: " + Global.currentBoard.getScore(), "Times up!", JOptionPane.INFORMATION_MESSAGE);
    }
}