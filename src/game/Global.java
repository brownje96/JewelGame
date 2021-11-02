package game;

import game.reporter.ErrorConsole;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * This class contains all objects needed program-wide by the game.
 */
public class Global {
    public static final Random rng = new Random();
    public static Game currentGame;
    public static Window mainWindow;
    public static ErrorConsole console;
    public static final BufferedImage[] sprites = new BufferedImage[7];
}