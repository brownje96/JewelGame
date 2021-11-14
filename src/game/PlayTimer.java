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
        bar.setStringPainted(true);
        bar.setValue(100);
        // timing
        while(remainingTime > 0) {
            try { Thread.sleep(1000);
            } catch (InterruptedException e) { return; }
            if(!Global.currentBoard.isPaused()) {
                remainingTime--;
                bar.setValue((int) ((((double) remainingTime) / ((double)initialTime)) * 100));
                // this next line adapted from Chathura Liyanage's example on StackOverflow
                bar.setString(String.format(
                    "%02d:%02d:%02d",
                    (remainingTime / 3600),
                    ((remainingTime % 3600) / 60),
                    (remainingTime % 60)
                ));
            } else Thread.yield();
        }
        Global.currentBoard.deSelectAllJewels();
        Global.mainWindow.lockBoard();
        JOptionPane.showMessageDialog(Global.mainWindow, "Your score is: " + Global.currentBoard.getScore(), "Times up!", JOptionPane.INFORMATION_MESSAGE);
    }
}