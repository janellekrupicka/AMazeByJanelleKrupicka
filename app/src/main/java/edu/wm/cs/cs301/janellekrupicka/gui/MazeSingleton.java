package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.Maze;

public class MazeSingleton {
    private Maze maze;
    public Maze getMaze() {return maze;}
    public void setMaze(Maze config) {this.maze = config;}

    private static final MazeSingleton mazeHolder = new MazeSingleton();
    public static MazeSingleton getInstance() {return mazeHolder;}
}

