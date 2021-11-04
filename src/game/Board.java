package game;

import java.awt.*;
import java.util.ArrayList;

import game.jewels.*;

/**
 * This class contains the logic for the game.
 */
public class Board {
    // attributes
    private final Dimension gameSize;
    private final Jewel[][] board;

    private int score = 0;

    // constructor

    /**
     * Creates a new Game
     *
     * @param x the width of the board
     * @param y the height of the board.
     */
    public Board(int x, int y) {
        gameSize = new Dimension(x,y);
        board = new Jewel[y][x];
        populate();
        onChange();
    }

    // game info

    /**
     * Gets the size of the board/play-field
     *
     * @return a Dimension object containing the size of the board.
     */
    public Dimension getGameSize() { return gameSize; }

    /**
     * Returns the board; a two-dimensional array of Jewel.
     *
     * @return the board.
     */
    public Jewel[][] getArray() {
        return board;
    }

    /**
     * Gets the score earned during this game.
     *
     * @return the score
     */
    public int getScore() { return score; }

    // Game Logic

    /**
     * Called when a change is made on the board.
     */
    public void onChange() {
        checkSelected();
        do {
            doGravity();
            populate();
        } while (checkForMatches(false));
        if((Global.mainWindow != null) && (Global.mainWindow.getGame() != null)) Global.mainWindow.updateBoard();
    }

    /**
     * Gravitates all floating jewels "down" to "Earth".
     */
    public void doGravity() {
        int[] lastEmpty = new int[2];
        boolean foundEmpty = false;
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = gameSize.height - 1; y >= 0; y--) {
                if((!foundEmpty) && (board[y][x] == null)) {
                    lastEmpty = new int[] {y, x};
                    foundEmpty = true;
                }
                if((foundEmpty) && (board[y][x] != null)) {
                    Jewel move = board[y][x];
                    board[y][x] = null;
                    board[lastEmpty[0]][lastEmpty[1]] = move;
                    foundEmpty = false;
                    lastEmpty = null;
                    y = gameSize.height;    // reset y.
                }
            }
            lastEmpty = null;
            foundEmpty = false;
        }
    }

    /**
     * Ensures that jewel selections are within the rules, and swaps them if they are.
     */
    private void checkSelected() {
        Jewel[] selected = getSelectedJewels();
        int selectedCount = selected.length;
        if(selectedCount <= 2) {
            if(selectedCount == 2) {
                int deltaX = Math.abs(getCoordinatesOf(selected[0])[1] - getCoordinatesOf(selected[1])[1]);
                int deltaY = Math.abs(getCoordinatesOf(selected[0])[0] - getCoordinatesOf(selected[1])[0]);
                if((deltaX + deltaY) > 1) deSelectAllJewels();  // too far away or diagonal swap requested
                else if ((deltaX + deltaY) == 1) {  // legal
                    swap(selected[0], selected[1]); // temporarily swap the two jewels
                    deSelectAllJewels();
                    if(checkForMatches(false)) doGravity(); // if we're good do gravitation
                    else swap(selected[0], selected[1]); // if we're not, put them back where they belong.
                }
            }
        } else {
            System.err.println("More than two jewels selected!");
            deSelectAllJewels();
        }
    }

    /**
     * Finds matches on the board, then removes them.
     *
     * @param removed should always be called with the value 'false', used to determine if during any recursion, any removals have occurred
     * @return returns the result of the parameter removed.
     */
    private boolean checkForMatches(boolean removed) {
        boolean recurse = false;
        Jewel[] x = checkX();
        Jewel[] y = checkY();
        if(x.length > 1) {
            recurse = true;
            removeJewels(x);
        }
        if(y.length > 1) {
            recurse = true;
            removeJewels(y);
        }
        if(recurse) {
            removed = true;
            checkForMatches(true);
        }
        return removed;
    }

    /**
     * Finds 3+ matches in the horizontal orientation
     *
     * @return an array of jewels
     */
    private Jewel[] checkX() {
        ArrayList<Jewel> toRemove = new ArrayList<>();
        String type = null;
        for(int y = 0; y < gameSize.height; y++) {
            for(int x = 0; x < gameSize.width; x++) {
                if(board[y][x] == null) continue;
                if(type == null) {
                    type = board[y][x].toString();
                    toRemove.add(board[y][x]);
                } else {
                    if(type.equals(board[y][x].toString())) toRemove.add(board[y][x]);
                    else {
                        if(toRemove.size() < 3) {
                            toRemove.clear();
                            type = null;
                            x--;
                        } else break;
                    }
                }
            }
            type = null;
            if(toRemove.size() >= 3) break;
            else toRemove.clear();
        }
        return toRemove.toArray(new Jewel[toRemove.size()]);
    }

    /**
     * Finds 3+ matches in the vertical orientation
     *
     * @return an array of jewels
     */
    public Jewel[] checkY() {
        ArrayList<Jewel> toRemove = new ArrayList<>();
        String type = null;
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = 0; y < gameSize.height; y++) {
                if(board[y][x] == null) continue;
                if(type == null) {
                    type = board[y][x].toString();
                    toRemove.add(board[y][x]);
                } else {
                    if(type.equals(board[y][x].toString())) toRemove.add(board[y][x]);
                    else {
                        if(toRemove.size() < 3) {
                            toRemove.clear();
                            type = null;
                            y--;
                        } else break;
                    }
                }
            }
            type = null;
            if(toRemove.size() >= 3) break;
            else toRemove.clear();
        }
        return toRemove.toArray(new Jewel[toRemove.size()]);
    }

    /**
     * Adds in new jewels for any spot on the board which is empty.
     */
    private void populate() {
        for(int y = 0; y < gameSize.height; y++) {
            for(int x = 0; x < gameSize.width; x++) {
                if(board[y][x] == null) board[y][x] = getRandomJewel();
            }
        }
    }

    // Utility

    /**
     * Removes a given set of jewels from the board
     *
     * @param list an array of jewels.
     */
    private void removeJewels(Jewel[] list) {
        score += list.length * 100;
        for(Jewel jewel : list) {
            int[] coordinate = getCoordinatesOf(jewel);
            if(!((coordinate[0] == -1)&&(coordinate[1] == -1))) board[coordinate[0]][coordinate[1]] = null;
        }
    }

    /**
     * Swaps the position of two jewels on the board.
     *
     * @param a first jewel to be swapped
     * @param b second jewel to be swapped
     */
    private void swap(Jewel a, Jewel b) {
        int[] Jewel_A_Coordinates = getCoordinatesOf(a), Jewel_B_Coordinates = getCoordinatesOf(b);
        board[Jewel_A_Coordinates[0]][Jewel_A_Coordinates[1]] = b;
        board[Jewel_B_Coordinates[0]][Jewel_B_Coordinates[1]] = a;
    }

    /**
     * Finds the coordinates of a jewel on the board
     *
     * @param j the jewel object
     * @return an array of int containing the coordinates, or (-1, -1) if it is not found on the board.
     */
    private int[] getCoordinatesOf(Jewel j) {
        for(int y = 0; y < gameSize.height; y++) {
            for(int x = 0; x < gameSize.width; x++) {
                if(board[y][x] == j) return new int[] { y, x };
            }
        }
        return new int[] {-1, -1};
    }

    /**
     * Gets an array of all Jewels where the selected flag is true.
     *
     * @return an array of game.jewels.Jewel
     */
    private Jewel[] getSelectedJewels() {
        ArrayList<Jewel> selected = new ArrayList<>();
        for(int y = 0; y < gameSize.height; y++) {
            for(int x = 0; x < gameSize.width; x++) {
                if(board[y][x].isSelected()) selected.add(board[y][x]);
            }
        }
        return selected.toArray(new Jewel[selected.size()]);
    }

    /**
     * Sets the "selected" flag back to false for all jewels on the board
     */
    public void deSelectAllJewels() {
        for(int y = 0; y < gameSize.height; y++)
            for(int x = 0; x < gameSize.width; x++)
                board[y][x].setSelected(false);
    }

    /**
     * Gets a random jewel. Presumably to be added to the board.
     *
     * @return a subclass of game.jewels.Jewel
     */
    public static Jewel getRandomJewel() {
        Jewel x = null;
        switch(Global.rng.nextInt(6)) {
            case 0:
                x= new Diamond();
                break;
            case 1:
                x= new Amethyst();
                break;
            case 2:
                x= new Emerald();
                break;
            case 3:
                x= new Ruby();
                break;
            case 4:
                x= new Sapphire();
                break;
            case 5:
                x= new Topaz();
                break;
            default:
                System.err.println("invalid gemstone added. This should never happen.");
                break;
        }
        return x;
    }

}