package game.reporter;

import game.Global;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// old code from 2016, slighly modified.
public class ErrorConsole
        extends JFrame {

    public ErrorConsole() {
        setLayout(new BorderLayout());
        setSize(640, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Fatal Error");
        setResizable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okBtn = new JButton("OK");
        buttonPanel.add(okBtn);
        okBtn.addActionListener(l -> System.exit(-1));

        add(new JLabel(readEntireFileAsStr(ErrorConsole.class.getResourceAsStream("bug.html"))), BorderLayout.NORTH);
        add(new JLabel(new ImageIcon(ErrorConsole.class.getResource("SLEEPLESS_DEV.gif"))), BorderLayout.WEST);
        add(new JScrollPane(sta), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        sta.getStyledDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Global.mainWindow.setEnabled(false);
                setVisible(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        revalidate();
    }

    public static String readEntireFileAsStr(InputStream f) {
        StringBuilder x = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(f))) {
            String s;
            while((s = br.readLine())!=null) x.append(s);
        } catch (IOException ex) { ex.printStackTrace(System.err); }
        return x.toString();
    }

    public final StreamableTextArea sta = new StreamableTextArea();
}