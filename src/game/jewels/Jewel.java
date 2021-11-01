package game.jewels;

import game.Global;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Jewel
    extends JComponent
    implements MouseListener {

    private final Image myIcon;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected = false;

    public Jewel(Image icon) {
        myIcon = icon;
        addMouseListener(this);
    }

    @Override protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(myIcon, 0, 0, getWidth(), getHeight(), null);
        if(selected) {
            g.setColor(Color.MAGENTA);
            //todo: see if I can avoid the *new* keyword here to save on memory.
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g.drawOval(0,0,getWidth(),getHeight());
        }
        // todo: I am super unsure of the safety of this call.
        getParent().repaint();
    }

    // sealed... for now.
    @Override public final void mouseClicked(MouseEvent e) {
        selected = !selected;
        Global.currentGame.onChange();
    }

    // ignored
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}