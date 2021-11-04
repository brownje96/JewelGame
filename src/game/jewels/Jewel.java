package game.jewels;

import game.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This is the base class for all Jewels.
 */
public class Jewel
    extends JComponent
    implements MouseListener {

    private final Image myIcon;
    private boolean selected = false;
    private final BasicStroke stroke = new BasicStroke(3);

    public Jewel(Image icon) {
        myIcon = icon;
        addMouseListener(this);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(myIcon, 0, 0, getWidth(), getHeight(), null);
        if(selected) {
            g.setColor(Color.MAGENTA);
            ((Graphics2D) g).setStroke(stroke);
            g.drawOval(0,0,getWidth(),getHeight());
        }
        getParent().repaint(); // todo: I am super unsure of the safety of this call.
    }

    // sealed... for now.
    @Override public final void mouseClicked(MouseEvent e) {
        // quick hack...
        if(isEnabled()) {
            selected = !selected;
            Global.currentBoard.onChange();
        }
    }

    // ignored
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}