package org.suckme.quicklauncher;

public class Coord {
    // Data
    private int x;
    private int y;

    // Constructor
    public Coord(int xIn, int yIn){
        x = xIn;
        y = yIn;
    }

    // Access functions
    public int getX(){ return x; }
    public int getY(){ return y; }

    // Modifiers
    public void setX(int xIn){ x = xIn; }
    public void setY(int yIn){ y = yIn; }
}