package game.ui.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Board;
import game.Global;
import game.Mode;

public final class GameConfigurator
    extends JPanel
    implements ActionListener {

    SpinnerNumberModel  snmX = new SpinnerNumberModel(8, 4, 100, 1),
                        snmY = new SpinnerNumberModel(8, 4, 100, 1),
                        snmT = new SpinnerNumberModel(60, 30, 600, 15);

    JSpinner x = new JSpinner(snmX), y = new JSpinner(snmY), t = new JSpinner(snmT);

    ButtonGroup gameModeSelectionGroup = new ButtonGroup();

    JRadioButton endlessOption = new JRadioButton("Endless"), timedOption = new JRadioButton("Timed");

    public GameConfigurator() {
        super(new GridLayout(4, 2));
        // configure button group
        gameModeSelectionGroup.add(endlessOption);
        gameModeSelectionGroup.add(timedOption);
        endlessOption.addActionListener(l -> t.setEnabled(false));
        timedOption.addActionListener(l -> t.setEnabled(true));
        endlessOption.setSelected(true);
        t.setEnabled(false);

        // add components
        add(new JLabel("X: "));
        add(x);
        add(new JLabel("Y: "));
        add(y);
        add(endlessOption);
        add(timedOption);
        add(new JLabel("Time: "));
        add(t);
    }

    @Override public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Global.mainWindow, this, "Define Size", JOptionPane.QUESTION_MESSAGE);
        Global.currentBoard = new Board((Integer) x.getValue(), (Integer) y.getValue());
        Global.mainWindow.updateGame(Global.currentBoard, endlessOption.isSelected()? Mode.ENDLESS : Mode.TIMED, (Integer) t.getValue());
    }

}