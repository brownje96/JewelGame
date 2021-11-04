package game.ui.actions;

import game.Global;
import game.components.WebPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class HelpViewer
    implements ActionListener {

    private static final String TITLE = "How to Play";
    private static final Dimension size = new Dimension(640, 480);
    private static final WebPane pane = new WebPane(size, HelpViewer.class.getResourceAsStream("/game/meta/help.html"));

    @Override public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Global.mainWindow, pane, TITLE, JOptionPane.INFORMATION_MESSAGE);
    }
}
