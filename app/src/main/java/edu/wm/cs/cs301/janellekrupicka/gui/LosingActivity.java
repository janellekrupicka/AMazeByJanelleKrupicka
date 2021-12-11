package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

/**
 * Losing screen if the robot runs out of energy or breaks before completing the maze.
 * Shows why the game was lost (from the robot running out of energy or breaking).
 * Takes data on path length, shortest path length, and energy consumption from the
 * controller and displays them. Includes option to restart the game by pressing back.
 *
 * Collaborators:
 * PlayAnimationActivity.java to get path length and energy level
 * AMazeActivity.java to go back to title screen
 */
public class LosingActivity extends AppCompatActivity {
    /**
     * Path length taken.
     */
    private int pathLengthInt;
    /**
     * Shortest possible path to take.
     */
    private int shortestPath;
    /**
     * True if robot crashed,
     * false if robot ran out of energy.
     */
    private boolean crashed;
    /**
     * Amount of energy consumed by robot.
     */
    private float energyConsumed;
    /**
     * Sets up layout for activity.
     * Sets the path length, shortest path, energy consumed,
     * crashed from extras from intent to get to activity.
     * Sets the text on layout based on extras.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        Intent intent = getIntent();
        if(null!=intent) {
            pathLengthInt = intent.getIntExtra("Path length", 500); // 500 for testing
            shortestPath = intent.getIntExtra("Shortest path length", 500);
            crashed = intent.getBooleanExtra("Reason for stop", true);
            energyConsumed = intent.getFloatExtra("Energy consumption", 500);
        }
        setText();
    }
    /**
     * Called in onCreate. Sets the text for losing based on
     * what occurred. Taken information from extras from intent to
     * get to this activity, saved in instance variables.
     */
    private void setText() {
        TextView robotStopReason = (TextView)findViewById(R.id.robotStopReason);
        if(crashed) robotStopReason.setText("Robot crashed.");
        if(!crashed) robotStopReason.setText("Robot ran out of energy.");

        TextView pathLength = (TextView)findViewById(R.id.pathLength);
        pathLength.setText("Path length: "+pathLengthInt);
        TextView shortestPathLength = (TextView)findViewById(R.id.shortestPathLength);
        shortestPathLength.setText("Shortest possible path length: "+shortestPath);
        TextView robotEnergyConsumption = (TextView)findViewById(R.id.robotEnergyConsumption);
        robotEnergyConsumption.setText("Amount of energy consumed: "+energyConsumed);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}