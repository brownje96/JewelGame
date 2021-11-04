package game;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import game.meta.Metadata;
import game.ui.windows.ErrorConsole;
import game.ui.windows.Window;

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
        doPlatformSpecificOperations();
        boolean hasGfx = !GraphicsEnvironment.getLocalGraphicsEnvironment().isHeadlessInstance();
        if(hasGfx) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    Global.console = new ErrorConsole();
                    System.setErr(new PrintStream(Global.console.sta.getInputStream(Color.RED)));
                    Global.loadResources();
                    Global.mainWindow = new Window();
                    Global.mainWindow.setVisible(true);
                });
            } catch (InterruptedException | InvocationTargetException e) {
                System.err.println("Could not open the game window. Compilation failure?");
                e.printStackTrace();
            }
        } else System.err.println("This JVM is 'headless' and does not have graphics support. Cannot continue.");
    }

    /**
     * Performs operations that are platform specific.
     *
     * with credit to:
     * https://stackoverflow.com/questions/22604218/set-a-dynamic-apple-menu-title-for-java-program-in-netbeans-7-4
     */
    public static void doPlatformSpecificOperations() {
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("mac")) {
            System.setProperty("apple.awt.application.name", Metadata.APP_NAME);
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.fileDialogForDirectories", "true");
        }
    }
}