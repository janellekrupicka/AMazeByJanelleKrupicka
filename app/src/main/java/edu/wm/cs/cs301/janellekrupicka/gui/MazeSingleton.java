package edu.wm.cs.cs301.janellekrupicka.gui;

import edu.wm.cs.cs301.janellekrupicka.generation.Maze;

public class MazeSingleton {
    private static Maze mazeInstance = null;
    public static Maze getMazeInstance() {
        return mazeInstance;
    }
    public static void setMazeInstance(Maze config) {
        mazeInstance = config;
    }
}
