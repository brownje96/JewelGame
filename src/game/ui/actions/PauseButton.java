package game.ui.actions;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import game.Board;
import game.Global;
import game.ui.windows.Window;

public class PauseButton
    implements MenuListener {

    private final JMenu btn;

    public PauseButton(JMenu button) { btn = button; }

    @Override public void menuSelected(MenuEvent e) {
        Window mainWindow = Global.mainWindow;
        Board cBoard = Global.currentBoard;
        if(btn.isEnabled()) {
            if(cBoard.isPaused()) {
                mainWindow.unlockBoard();
                btn.setText("PAUSE");
            } else {
                mainWindow.lockBoard();
                btn.setText("PLAY");
            }
            cBoard.setPaused(!cBoard.isPaused());
        }
    }

    @Override public void menuDeselected(MenuEvent e) {}
    @Override public void menuCanceled(MenuEvent e) {}
}