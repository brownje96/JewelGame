package game.ui.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Global;
import game.components.WebPane;
import game.meta.Metadata;

public final class AboutViewer
    implements ActionListener {

    private static final Dimension size = new Dimension(640, 480);
    private static final String title = String.format("About %s", Metadata.APP_NAME);
    private static final WebPane pane = new WebPane(
        size,
        AboutViewer.class.getResourceAsStream("/game/meta/credits.html"),
        Metadata.APP_NAME,
        Metadata.APP_AUTH,
        Metadata.APP_VERS
    );

    @Override public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Global.mainWindow, pane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}