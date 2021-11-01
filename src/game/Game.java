package game;

import game.jewels.*;
import java.awt.*;
import java.util.ArrayList;

public class Game {
    // attributes
    private final Dimension gameSize;
    private final Jewel[][] board = new Jewel[8][8];

    private int score = 0;

    // constructor
    public Game(int x, int y) {
        gameSize = new Dimension(x,y);
        populate();
        onChange();
    }

    // game info

    public Dimension getGameSize() { return gameSize; }

    public Jewel[][] getBoard() {
        return board;
    }

    public int getScore() { return score; }

    // Game Logic

    public void onChange() {
        checkSelected();
        do {
            doGravity();
            populate();
        } while (checkForMatches(false));
        if(Global.mainWindow != null) Global.mainWindow.updateBoard();
    }

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

    private void checkSelected() {
        Jewel[] selected = getSelectedJewels();
        int selectedCount = selected.length;
        if(selectedCount <= 2) {
            if(selectedCount == 2) {
                int deltaX = Math.abs(getCoordinatesOf(selected[0])[0] - getCoordinatesOf(selected[1])[0]);
                int deltaY = Math.abs(getCoordinatesOf(selected[0])[1] - getCoordinatesOf(selected[1])[1]);
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

    private Jewel[] checkX() {
        ArrayList<Jewel> toRemove = new ArrayList<>();
        String type = null;
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = 0; y < gameSize.height; y++) {
                if(board[x][y] == null) continue;
                if(type == null) {
                    type = board[x][y].toString();
                    toRemove.add(board[x][y]);
                } else {
                    if(type.equals(board[x][y].toString())) toRemove.add(board[x][y]);
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

    public Jewel[] checkY() {
        ArrayList<Jewel> toRemove = new ArrayList<>();
        String type = null;
        for(int y = 0; y < gameSize.height; y++) {
            for(int x = 0; x < gameSize.width; x++) {
                if(board[x][y] == null) continue;
                if(type == null) {
                    type = board[x][y].toString();
                    toRemove.add(board[x][y]);
                } else {
                    if(type.equals(board[x][y].toString())) toRemove.add(board[x][y]);
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

    private void populate() {
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = 0; y < gameSize.height; y++) {
                if(board[x][y] == null) board[x][y] = getRandomJewel();
            }
        }
    }

    // Utility

    private void removeJewels(Jewel[] list) {
        score += list.length * 100;
        for(Jewel jewel : list) {
            int[] coordinate = getCoordinatesOf(jewel);
            if(!((coordinate[0] == -1)&&(coordinate[1] == -1))) board[coordinate[0]][coordinate[1]] = null;
        }
    }

    private void swap(Jewel a, Jewel b) {
        int[] Jewel_A_Coordinates = getCoordinatesOf(a), Jewel_B_Coordinates = getCoordinatesOf(b);
        board[Jewel_A_Coordinates[0]][Jewel_A_Coordinates[1]] = b;
        board[Jewel_B_Coordinates[0]][Jewel_B_Coordinates[1]] = a;
    }

    private int[] getCoordinatesOf(Jewel j) {
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = 0; y < gameSize.height; y++) {
                if(board[x][y] == j) {
                    return new int[] { x, y };
                }
            }
        }
        return new int[] {-1, -1};
    }

    private Jewel[] getSelectedJewels() {
        ArrayList<Jewel> selected = new ArrayList<>();
        for(int x = 0; x < gameSize.width; x++) {
            for(int y = 0; y < gameSize.height; y++) {
                if(board[x][y].isSelected()) selected.add(board[x][y]);
            }
        }
        return selected.toArray(new Jewel[selected.size()]);
    }

    private void deSelectAllJewels() {
        for(int x = 0; x < gameSize.width; x++)
            for(int y = 0; y < gameSize.height; y++)
                board[x][y].setSelected(false);
    }

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