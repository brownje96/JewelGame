package game;

import game.meta.Metadata;
import game.reporter.ErrorConsole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * This class initializes the game and loads all resources.
 */
public class Loader {
    /**
     * The entry point of the program.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        System.setProperty("apple.awt.application.name", Metadata.APP_NAME);
        boolean hasGfx = !GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
        if(hasGfx) {
            //todo: size customization?
            try {
                SwingUtilities.invokeAndWait(() -> {
                    Global.console = new ErrorConsole();
                    System.setErr(new PrintStream(Global.console.sta.getInputStream(Color.RED)));
                    loadResources();
                    Global.mainWindow = new Window();
                    Global.mainWindow.setVisible(true);
                });
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Could not open the game window. Compilation failure?");
                e.printStackTrace();
            }
        } else System.err.println("This JVM is 'headless' and does not have graphics support. Cannot continue.");
    }

    private static void loadResources() {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Loader.class.getResourceAsStream("/sheet.png"));
        } catch (IOException e) {
            System.err.println("Could not load sprite sheet. Quitting.");
            e.printStackTrace();
            return;
        }
        for(int i = 0; i < Global.sprites.length; i++) Global.sprites[i] = sheet.getSubimage(32 * i, 0, 32, 32);
    }
}