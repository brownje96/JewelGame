package game;

import game.ui.windows.ErrorConsole;
import game.ui.windows.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * This class contains all objects needed program-wide by the game.
 */
public class Global {
    // global objects
    public static final Random rng = new Random();
    public static Board currentBoard;

    // windows
    public static Window mainWindow;
    public static ErrorConsole console;

    // resources
    public static final BufferedImage[] sprites = new BufferedImage[7];

    // methods
    public static void loadResources() {
        BufferedImage sheet;
        try {
            sheet = ImageIO.read(Objects.requireNonNull(Global.class.getResourceAsStream("/sheet.png")));
        } catch (IOException e) {
            System.err.println("Could not load sprite sheet. Quitting.");
            e.printStackTrace();
            return;
        }
        for(int i = 0; i < Global.sprites.length; i++) Global.sprites[i] = sheet.getSubimage(32 * i, 0, 32, 32);
    }
}