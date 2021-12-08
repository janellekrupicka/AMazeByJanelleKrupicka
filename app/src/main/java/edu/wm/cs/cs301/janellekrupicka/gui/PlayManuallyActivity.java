package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

import edu.wm.cs.cs301.janellekrupicka.generation.Maze;

/**
 * The UI to play the maze manually.
 * Shows the maze and the user can move through the maze using the up, down, left, and right buttons.
 * Can show the top down map with or without the solution.
 * Can also increase or decrease the map scale.
 * Temporarily shows an option to continue to the next state (State Winning) with a shortcut.
 * Typically, moves to State Winning when the user gets to the end of the maze.
 * The user can select Back and move back to State Title.
 * Keeps track of path length by counting the number of times the forward arrow is pressed.
 *
 * Collaborators:
 * Controller.java to move forward, rotate, toggle map, and toggle map solution.
 * AMazeActivity.java to return to the title screen.
 * WinningActivity.java to move to if the robot reaches the exit.
 */
public class PlayManuallyActivity extends AppCompatActivity {
    private int pathLength;
    private Maze maze;
    private StatePlaying statePlaying;
    private int skillLevel;
    private int shortestPath;
    /**
     * Sets up layout for activity.
     * Starts onCheckedChangeListener for show map toggle,
     * show solution toggle, and show visible walls toggle.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        statePlaying = new StatePlaying();
        statePlaying.setActivityManual(this);
        Intent intent = getIntent();
        skillLevel = intent.getIntExtra("Skill level", 0);
        pathLength = 0;
        maze=MazeSingleton.getInstance().getMaze();
        MazePanel mazePanel = findViewById(R.id.maze_panel);
        statePlaying.start(mazePanel);
        shortestPath = statePlaying.getDistanceToExit();
    //    mazePanel.commit();
    }
    public void setStatePlaying(StatePlaying statePlaying) {
        this.statePlaying = statePlaying;
    }
    public void moveToNextActivity() {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Path length", pathLength); // will get from controller
        intent.putExtra("Shortest path length", shortestPath); // will get from controller
        startActivity(intent);
        finish();
    }
    /**
     * Called when ShortCut button is selected.
     * Sends extras (path length, shortest path length) to next
     * activity to display on winning screen.
     * Starts next activity (WinningActivity).
     * @param view
     */
    public void shortCut(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Path length", pathLength); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        startActivity(intent);
    }
    /**
     * Method called when the plus button is selected to increase map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void increaseMapScale(View view) {
        statePlaying.keyDown(Constants.UserInput.ZOOMIN, skillLevel);
        Toast.makeText(getBaseContext(), "Map scale increased", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Map scale increase selected");
    }
    /**
     * Method called when the minus button is selected to decrease map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void decreaseMapScale(View view) {
        statePlaying.keyDown(Constants.UserInput.ZOOMOUT, skillLevel);
        Toast.makeText(getBaseContext(), "Map scale decreased", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Map scale decrease selected");
    }
    /**
     * Called when forward arrow is selected. Will move the player forward.
     * Collaborate with controller to move robot.
     * Puts toast message and log.v output when selected.
     * @param view
     */
    public void moveForward(View view) {
        statePlaying.keyDown(Constants.UserInput.UP, skillLevel);
        pathLength++;
        Toast.makeText(getBaseContext(), "Move forward", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Forward button selected");
    }
    /**
     * Called when left arrow is selected. Will turn the player left.
     * Collaborate with controller to move robot.
     * Puts toast message and log.v output when selected.
     * @param view
     */
    public void turnLeft(View view) {
        statePlaying.keyDown(Constants.UserInput.LEFT, skillLevel);
        Toast.makeText(getBaseContext(), "Turn left", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Left button selected");
    }
    /**
     * Called when right arrow is selected. Will turn the player right.
     * Collaborate with controller to move robot.
     * Puts toast message and log.v output when selected.
     * @param view
     */
    public void turnRight(View view) {
        statePlaying.keyDown(Constants.UserInput.RIGHT, skillLevel);
        Toast.makeText(getBaseContext(), "Turn right", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Right button selected");
    }
    /**
     *
     */
    public void showMap(View view) {
        Log.v("PlayManuallyActivity", "Show map turned on/off");
        statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, skillLevel);

    }
    /**
     * Starts onCheckedChangeListener for show solution toggle button.
     * Will show solution when is checked, currently just shows
     * toast and log.v output when toggle is selected.
     */
    public void showSolution(View view) {
        statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, skillLevel);
       // Toast.makeText(getBaseContext(), "Showing solution", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Show map turned on/off");

    }
    public void showVisibleWalls(View view) {
        statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, skillLevel);
    //    Toast.makeText(getBaseContext(), "Showing visible walls", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Show visible walls turned on/oof");
    }

}