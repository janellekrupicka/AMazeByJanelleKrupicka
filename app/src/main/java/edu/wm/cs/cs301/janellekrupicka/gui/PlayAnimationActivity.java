package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

import edu.wm.cs.cs301.janellekrupicka.generation.Distance;
import edu.wm.cs.cs301.janellekrupicka.generation.Maze;

/**
 * The UI to play the game with the driver and robot.
 * Shows the robot moving through the maze.
 * Shows which of the robot’s sensors are currently operational.
 * Includes an option to toggle on the map and solution rendering along with an option
 * to adjust the map scale. Has a pause/play button that lets the user pause the robot as it
 * moves through the maze. Also, includes a bar where the user can adjust the speed the robot
 * travels through the maze. It also shows the energy level of the robot going through the maze.
 * Includes a back button that returns to the title screen.
 *
 * Collaborators:
 * Controller.java
 * Robot.java
 * WinningActivity.java, LosingActivity.java
 */
public class PlayAnimationActivity extends AppCompatActivity {
    private String driverType;
    private String robotType;
    private Maze maze;
    private StatePlaying statePlaying;
    private int skillLevel;
    private int shortestPath;
    /**
     * Sets up layout for activity.
     * Gets extras from intent that sent to this activity.
     * Starts onSeekBarChangeListener for animation speed SeekBar.
     * Starts onCheckedListener for map toggle switch.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
        statePlaying = new StatePlaying();
        statePlaying.setActivityAnimated(this);
        Log.v("PlayAnimationActivity", "Set activity to animated");
        Intent intent = getIntent();
        driverType=intent.getStringExtra("Driver type");
        robotType=intent.getStringExtra("Robot type");
        skillLevel = intent.getIntExtra("Skill level", 0);
        maze=MazeSingleton.getInstance().getMaze();
        getAnimationSpeed();
        showMap();
        maze=MazeSingleton.getInstance().getMaze();
        MazePanel mazePanel = findViewById(R.id.maze_panel);
        statePlaying.start(mazePanel);
        shortestPath = statePlaying.getDistanceToExit();
        startAnimation();
    }
    public void moveToNextActivity() {
        Intent intent = new Intent(this, WinningActivity.class);
        // will need for be changed so the values are no longer hard coded
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", shortestPath); // will get from controller
        intent.putExtra("Energy consumption", 0); // will get from controller
        startActivity(intent);
    }
    private void startAnimation() {
        if(driverType.equals("Wizard")) {
            Wizard wizard = new Wizard();
            ReliableRobot robot = new ReliableRobot();
            robot.setStatePlaying(statePlaying);
            DistanceSensor forward  = new ReliableSensor();
            forward.setSensorDirection(Robot.Direction.FORWARD);
            DistanceSensor left = new ReliableSensor();
            left.setSensorDirection(Robot.Direction.LEFT);
            DistanceSensor backward = new ReliableSensor();
            backward.setSensorDirection(Robot.Direction.BACKWARD);
            DistanceSensor right = new ReliableSensor();
            right.setSensorDirection(Robot.Direction.RIGHT);
            robot.addDistanceSensor(forward, Robot.Direction.FORWARD);
            robot.addDistanceSensor(left, Robot.Direction.LEFT);
            robot.addDistanceSensor(right, Robot.Direction.RIGHT);
            robot.addDistanceSensor(backward, Robot.Direction.BACKWARD);
            wizard.setRobot(robot);
            wizard.setMaze(maze);
            try {
                wizard.drive2Exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Method called when Go2Winning is selected.
     * Creates intent to to go to WinningActivity.
     * Sends with extras about path length, shortest path length,
     * and energy consumption.
     * @param view
     */
    public void go2Winning(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        // will need for be changed so the values are no longer hard coded
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        intent.putExtra("Energy consumption", 0); // will get from controller
        startActivity(intent);
    }
    /**
     * Method called when Go2Losing is selected.
     * Creates intent to to go to LosingActivity.
     * Sends with extras about path length, shortest path length,
     * and energy consumption.
     * @param view
     */
    public void go2Losing(View view) {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        intent.putExtra("Energy consumption", 0); // will get from controller
        intent.putExtra("Reason for stop", true);// true if crashed/broke, false if ran out of energy
        startActivity(intent);
    }
    /**
     * Method called when the plus button is selected to increase map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void increaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale increased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale increased");
    }
    /**
     * Method called when the minus button is selected to decrease map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void decreaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale decreased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale decreased");
    }
    /**
     * Method called when the play button is selected to play animation.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually play animation.
     * @param view
     */
    public void pressPlay(View view) {
        Toast.makeText(getBaseContext(), "Playing animation", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Playing animation");
    }
    /**
     * Method called when the pause button is selected to pause animation.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually pause animation.
     * @param view
     */
    public void pressPause(View view) {
        Toast.makeText(getBaseContext(), "Animation paused", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Animation paused");
    }
    public void showMap(View view) {
        Log.v("PlayManuallyActivity", "Show map turned on/off");
    //    statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, skillLevel);
    }
    /**
     * Starts onCheckedChangeListener for show solution toggle button.
     * Will show solution when is checked, currently just shows
     * toast and log.v output when toggle is selected.
     */
    public void showSolution(View view) {
    //    statePlaying.keyDown(Constants.UserInput.TOGGLESOLUTION, skillLevel);
        // Toast.makeText(getBaseContext(), "Showing solution", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Show map turned on/off");

    }
    public void showVisibleWalls(View view) {
    //    statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, skillLevel);
        //    Toast.makeText(getBaseContext(), "Showing visible walls", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Show visible walls turned on/oof");
    }
    /**
     * Method to change sensor indicator color based on whether
     * sensor is operational. (Not yet used, will collaborate with robot).
     * @param direction: sensor direction
     * can be forward, backward, left, or right
     * @param isOperational
     */
    private void setSensorOperational(String direction, boolean isOperational) {
        int color = ContextCompat.getColor(this, R.color.green);
        if(!isOperational) {
            color = ContextCompat.getColor(this, R.color.red);
        }
        if(direction.equals("forward")) {
            TextView sensor = findViewById(R.id.sensor_forward);
            sensor.setBackgroundColor(color);
        }
        if(direction.equals("backward")) {
            TextView sensor = findViewById(R.id.sensor_backward);
            sensor.setBackgroundColor(color);
        }
        if(direction.equals("left")) {
            TextView sensor = findViewById(R.id.sensor_left);
            sensor.setBackgroundColor(color);
        }
        if(direction.equals("right")) {
            TextView sensor = findViewById(R.id.sensor_right);
            sensor.setBackgroundColor(color);
        }
    }
    /**
     * showMap toggle button onCheckedChangeListener.
     * Will put a toast message and log.v output when
     * button is turned on or off to show map.
     */
    private void showMap() {
        Switch showMap=(Switch) findViewById(R.id.show_map2);
        // code for onCheckedChangeListener from
        // https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
        showMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked) {
                   Toast.makeText(getBaseContext(), "Showing map", Toast.LENGTH_SHORT).show();
                   Log.v("PlayAnimationActivity", "Show map turned on");
               }
               if(!isChecked) {
                Toast.makeText(getBaseContext(), "Not showing map", Toast.LENGTH_SHORT).show();
                Log.v("PlayAnimationActivity", "Show map turned off");
               }
            }
        });
    }
    /**
     * To start onSeekBarChangeListener for animation speed seek bar.
     * When seek bar is adjusted for animation speed, shows
     * toast and log.v output with the animation speed it's set to.
     * @return
     */
    private int getAnimationSpeed() {
        SeekBar animationSpeed=(SeekBar) findViewById(R.id.animationSpeed);
        animationSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getBaseContext(), "Animation speed: "+animationSpeed.getProgress(), Toast.LENGTH_SHORT).show();
                Log.v("PlayAnimationActivity", "Animation speed selected: "+animationSpeed.getProgress());
            }
        });
        return animationSpeed.getProgress();
    }
    /**
     * Will be used to set the energy level for the progress bar.
     * That shows the robot's energy level. Will collaborate with robot/controller.
     * @param energy
     */
    private void setEnergyLevel(int energy) {
        ProgressBar energyLevel=(ProgressBar) findViewById(R.id.energyBar);
        energyLevel.setProgress(energy);
    }
}