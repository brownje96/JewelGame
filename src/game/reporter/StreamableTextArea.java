package game.reporter;

import java.awt.*;
import java.io.OutputStream;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class StreamableTextArea
        extends JTextPane {

    public StreamableTextArea() {
        setEditable(false);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    public OutputStream getInputStream() {
        return getInputStream(null);
    }

    public OutputStream getInputStream(final Color c) {
        final SimpleAttributeSet fmtAttr = new SimpleAttributeSet();
        if(c != null) StyleConstants.setForeground(fmtAttr, c);
        return new OutputStream() {

            final StyledDocument d = getStyledDocument();

            @Override public void write(int arg0) {
                try {
                    d.insertString(
                        d.getLength(),
                        Character.isValidCodePoint(arg0) ? Character.toString((char) arg0) : "?",
                        (c==null)?null:fmtAttr
                    );
                } catch (BadLocationException e) { e.printStackTrace(); }
                setCaretPosition(d.getLength());
            }
        };
    }

}