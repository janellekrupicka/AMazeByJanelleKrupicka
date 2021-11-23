package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

/**
 * Winning screen if the user or robot completes the maze.
 * Takes data on path length, shortest path length, and energy consumption from the controller
 * and displays them. Includes option to restart the game by pressing back.
 *
 * Collaborators:
 * AMazeActivity.java to go back (parent activity)
 * PlayManuallyActivity -- to get extras
 * PlayAnimationActivity -- to get extras
 */
public class WinningActivity extends AppCompatActivity {
    /**
     * Path length taken.
     */
    private int pathLength;
    /**
     * Shortest possible path length.
     */
    private int shortestPath;
    /**
     * Whether the game was played manually or not.
     */
    private boolean manual;
    /**
     * Energy consumed by robot if not played manually.
     */
    private int energyConsumed;
    /**
     * Sets up layout for activity.
     * Sets the path length and shortest path.
     * Shows energy consumption only if reached from
     * PlayAnimationActivity.
     * Will only have extra "Energy consumption" if
     * comes from PlayAnimationActivity.
     * Sets the text on layout based on extras.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
        Intent intent = getIntent();
        if(null!=intent) {
            pathLength = intent.getIntExtra("Path length", 500);
            shortestPath = intent.getIntExtra("Shortest path length", 500);
            if(intent.hasExtra("Energy consumption")) {
                manual = false;
                energyConsumed = intent.getIntExtra("Energy consumption", 500);
            }
            if(!intent.hasExtra("Energy consumption")) {
                manual = true;
            }
        }
        setText();
    }
    /**
     * Called in onCreate. Sets the text for winning based on
     * what occurred. Taken information from extras from intent to
     * get to this activity, saved in instance variables.
     */
    private void setText() {
        TextView pathLength2 = (TextView)findViewById(R.id.pathLength2);
        pathLength2.setText("Path length: "+pathLength);
        TextView shortestPathLength2 = (TextView)findViewById(R.id.shortestPathLength2);
        shortestPathLength2.setText("Shortest possible path length: "+shortestPath);

        TextView robotEnergyConsumption = (TextView)findViewById(R.id.robotEnergyConsumption2);
        if(manual) robotEnergyConsumption.setVisibility(View.GONE);
        if(!manual) robotEnergyConsumption.setText("Amount of energy consumed: "+energyConsumed);
    }

}
