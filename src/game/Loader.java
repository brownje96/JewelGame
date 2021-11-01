package game;

import game.reporter.ErrorConsole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

public class Loader {

    public static void main(String[] args) {
        boolean hasGfx = !GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
        if(hasGfx) {
            BufferedImage sheet;
            try {
                sheet = ImageIO.read(Loader.class.getResourceAsStream("/sheet.png"));
            } catch (IOException e) {
                System.err.println("Could not load sprite sheet. Quitting.");
                e.printStackTrace();
                return;
            }
            for(int i = 0; i < Global.sprites.length; i++) Global.sprites[i] = sheet.getSubimage(32 * i, 0, 32, 32);
            //todo: size customization?
            Global.currentGame = new Game(8, 8);
            try {
                SwingUtilities.invokeAndWait(() -> {
                    Global.console = new ErrorConsole();
                    System.setErr(new PrintStream(Global.console.sta.getInputStream(Color.RED)));

                    Global.mainWindow = new Window();
                    Global.mainWindow.setVisible(true);
                    Global.mainWindow.updateGame(Global.currentGame);
                });
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Could not open the game window. Compilation failure?");
                e.printStackTrace();
            }
        } else System.err.println("This JVM is 'headless' and does not have graphics support. Cannot continue.");
    }
}