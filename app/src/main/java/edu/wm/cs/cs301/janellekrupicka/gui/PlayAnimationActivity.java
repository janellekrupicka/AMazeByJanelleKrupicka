package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private Handler aniHandler = new Handler();
    private Wizard wizard;
    private Robot robot;
    private int delayTime;
    private boolean play;
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
   //     Log.v("PlayAnimationActivity", "Set activity to animated");
        Intent intent = getIntent();
        delayTime = 300;
        driverType=intent.getStringExtra("Driver type");
        robotType=intent.getStringExtra("Robot type");
        skillLevel = intent.getIntExtra("Skill level", 0);
        maze=MazeSingleton.getInstance().getMaze();
    //    maze = GeneratingActivity.mazeFinal;
    //    MazeSingleton.getInstance().setMaze(null);
        getAnimationSpeed();
    //    showMap();
    //    maze=MazeSingleton.getInstance().getMaze();
        MazePanel mazePanel = findViewById(R.id.maze_panel);
        statePlaying.start(mazePanel);
        shortestPath = statePlaying.getDistanceToExit();
        play = true;
        startAnimation();
    }
    private boolean stopped;
    public void moveToNextActivity() {
    //    updateAnimation.join();
    //    Log.v("PlayAnimationActivity", "about to remove callbacks");
        aniHandler.removeCallbacks(updateAnimation);
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Path length", robot.getOdometerReading()); // will get from controller
        intent.putExtra("Shortest path length", shortestPath); // will get from controller
        intent.putExtra("Energy consumption", wizard.getEnergyConsumption()); // will get from controller
        startActivity(intent);
        MazeSingleton.getInstance().setMaze(null);
        finish();

    }
    private void moveToLosing() {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("Path length", robot.getOdometerReading()); // will get from controller
        intent.putExtra("Shortest path length", shortestPath); // will get from controller
        intent.putExtra("Energy consumption", wizard.getEnergyConsumption()); // will get from controller
        intent.putExtra("Reason for stop", stopped);// true if crashed/broke, false if ran out of energy
        startActivity(intent);
        MazeSingleton.getInstance().setMaze(null);
        finish();
    }
    private void startAnimation() {
        if(driverType.equals("Wizard")) {
            wizard = new Wizard();
            robot = new ReliableRobot();
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
                //        aniHandler.removeCallbacks(updateAnimation);
            aniHandler.postDelayed(updateAnimation, delayTime);

        }
    }
    private boolean win = false;
    public void setWin(boolean yesWin) {
        win = yesWin;
    }
    private Runnable updateAnimation = new Runnable() {
        @Override
        public void run() {
            int[] curPosition = {0, 0};
            try {
                wizard.drive1Step2Exit();
            } catch (Exception e) {
            //    e.printStackTrace();
                try {
                    if(!win) {
                        stopped = true;
                        MazeSingleton.getInstance().setMaze(null);
                        moveToLosing();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return;
            }
        //    }
            setEnergyLevel(robot.getBatteryLevel());
            aniHandler.postDelayed(this, delayTime);
        }
    };
    public void showMap(View view) {
        statePlaying.keyDown(Constants.UserInput.TOGGLELOCALMAP, skillLevel);
        statePlaying.keyDown(Constants.UserInput.TOGGLEFULLMAP, skillLevel);
    //    Toast.makeText(getBaseContext(), "Showing map", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Show map turned on");
    }
    /**
     * Method called when the plus button is selected to increase map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void increaseMapScale(View view) {
        statePlaying.keyDown(Constants.UserInput.ZOOMIN, skillLevel);
    //    Toast.makeText(getBaseContext(), "Map scale increased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale increased");
    }
    /**
     * Method called when the minus button is selected to decrease map scale.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually adjust map scale.
     * @param view
     */
    public void decreaseMapScale(View view) {
        statePlaying.keyDown(Constants.UserInput.ZOOMOUT, skillLevel);
    //    Toast.makeText(getBaseContext(), "Map scale decreased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale decreased");
    }
    /**
     * Method called when the play button is selected to play animation.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually play animation.
     * @param view
     */
    public void pressPlay(View view) {
        aniHandler.postDelayed(updateAnimation, delayTime);
    //    play = true;
    //    Toast.makeText(getBaseContext(), "Playing animation", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Playing animation");
    }
    /**
     * Method called when the pause button is selected to pause animation.
     * Puts out toast message and log.v output when selected.
     * Will collaborate to actually pause animation.
     * @param view
     */
    public void pressPause(View view) {
        aniHandler.removeCallbacks(updateAnimation);
    //    play = false;
    //    Toast.makeText(getBaseContext(), "Animation paused", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Animation paused");
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
                delayTime = animationSpeed.getProgress();
            //    Toast.makeText(getBaseContext(), "Animation speed: "+animationSpeed.getProgress(), Toast.LENGTH_SHORT).show();
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
    private void setEnergyLevel(float energy) {
        ProgressBar energyLevel=(ProgressBar) findViewById(R.id.energyBar);
        energyLevel.setProgress((int) energy);
    }
    // for debugging
    private void senseAtExit() throws Exception {
        Log.v("PlayAnimationActivity", "Distance forward: "+robot.distanceToObstacle(Robot.Direction.FORWARD));
        Log.v("PlayAnimationActivity", "Distance backward: "+robot.distanceToObstacle(Robot.Direction.BACKWARD));
        Log.v("PlayAnimationActivity", "Distance left: "+robot.distanceToObstacle(Robot.Direction.LEFT));
        Log.v("PlayAnimationActivity", "Distance right: "+robot.distanceToObstacle(Robot.Direction.RIGHT));
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        aniHandler.removeCallbacks(updateAnimation);
        return true;
    }
}