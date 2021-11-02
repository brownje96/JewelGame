package game.components;

import game.Utility;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class WebPane
    extends JEditorPane {

    public WebPane(Dimension size, InputStream file) {
        this(size, file, (Object) null);
    }

    public WebPane(Dimension size, InputStream file, Object... args) {
        super("text/html", String.format(Utility.readEntireFileAsStr(file), args));
        setEditable(false);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
    }

}
