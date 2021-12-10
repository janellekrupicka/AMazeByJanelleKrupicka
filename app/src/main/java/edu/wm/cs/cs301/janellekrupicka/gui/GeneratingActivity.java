package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

import java.util.Random;

import edu.wm.cs.cs301.janellekrupicka.generation.Factory;
import edu.wm.cs.cs301.janellekrupicka.generation.Maze;
import edu.wm.cs.cs301.janellekrupicka.generation.MazeFactory;
import edu.wm.cs.cs301.janellekrupicka.generation.Order;

/**
 * Maze generating state for the UI.
 * Sets the driver, the robot, and can go back to AMazeActivity.java.
 * Keeps track of the maze being built with a loading bar.
 * Once the maze is fully loaded, check if a driver has been selected.
 * If the driver has been selected, move onto PlayManually or PlayAnimation.
 * If the driver hasn’t been selected, give a prompt to have the user select a driver.
 * If the driver isn’t manual, make sure that robot type is selected before moving onto PlayAnimation.
 *
 * Collaborators:
 * Controller.java to set driver, robot (sensors for robot based on input).
 * MazeFactory.java to wait for maze to be built shown with loading bar.
 * AMazeActivity.java to go back to with the back button.
 * PlayManuallyActivity.java to go to next if Manual is selected as driver.
 * PlayAnimationActivity.java to go to next if Wizard or Wallfollower is selected as driver.
 */
public class GeneratingActivity extends AppCompatActivity implements Order {
    private int skillLevel;
    private boolean perfect;
    private Builder mazeGenAlgorithm;
    private String driverType;
    private String robotType;
    private int seed;
    private int percentdone;
    private Factory factory;
    private boolean deterministic = false;
    /**
     * Loading maze progress.
     */
    private static int progress;
    /**
     * ProgressBar that shows the loading maze progress.
     */
    private ProgressBar progressBar;
    /**
     * Status to adjust for increase in progress.
     */
    private int progressStatus = 0;
    /**
     * Handler for thread.
     */
    private Handler handler = new Handler();

    /**
     * Sets up layout for GeneratingActivity.
     * Gets extras from intent that sends to GeneratingActivity.
     * Set up onItemSelectedListener for robot type spinner.
     * Creates a background thread to run progressBar.
     * Uses handler.post to check if driver/play type has been
     * selected after progressBar completes.
     * If driver hasn't been selected, puts a toast message to
     * tell user to select a driver
     * and starts onItemSelectedListener for driver spinner.
     * If a driver is already selected, puts a toast message
     * and log.v output with that driver selection
     * and moves to PlayManuallyActivity or PlayAnimation activity depending
     * on driver selected.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        // get extras from AMazeActivity
        Intent intent = getIntent();
        skillLevel = intent.getIntExtra("Skill level", 0);
        Log.v("GeneratingActivity", "Skill level check:"+skillLevel);
        perfect = !intent.getBooleanExtra("Rooms", true);
        seed = intent.getIntExtra("Seed", 13);
        builderFromString(intent.getStringExtra("Maze gen algorithm"));
        //  mazeGenAlgorithm=intent.getStringExtra("Maze gen algorithm");
        // set default robotType and start onItemSelectedListener
        robotType = getRobotType();
        // code for background thread from
        // http://www.java2s.com/Code/Android/UI/UsingThreadandProgressbar.htm
        //    progress = 0;
        //    progressBar = (ProgressBar) findViewById(R.id.loadingBar);
        //    progressBar.setMax(100);
        buildMazeConfig();
        factory.waitTillDelivered();
        playTypeObserver();
    }
    private void playTypeObserver() {
        Spinner play_type = findViewById(R.id.play_type);
        String selectedPlay = play_type.getItemAtPosition(play_type.getSelectedItemPosition()).toString();
        if (selectedPlay.equals("None selected")) {
            // tell user to select a driver
        //    Toast.makeText(getBaseContext(), "Select driver to continue.", LENGTH_LONG).show();
            playTypeSpinnerSelection();
        }
        else if (selectedPlay.equals("Manual")) {
            driverType = "Manual";
        //    Toast.makeText(getBaseContext(), "Playing manually.", Toast.LENGTH_SHORT).show();
            Log.v("GeneratingActivity", "Playing manually.");
            // go to play manually
            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
            setExtrasForIntent(intent);
            startActivity(intent);
            finish();
        }
        else if (selectedPlay.equals("Wizard")) {
            driverType = "Wizard";
        //    Toast.makeText(getBaseContext(), "Playing with Wizard.", Toast.LENGTH_SHORT).show();
            Log.v("GeneratingActivity", "Playing with Wizard.");
            // go to play animation
            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
            setExtrasForIntent(intent);
            startActivity(intent);
            finish();
        }
        else if (selectedPlay.equals("Wallfollower")) {
            driverType = "Wallfollower";
        //    Toast.makeText(getBaseContext(), "Playing with Wallfollower.", Toast.LENGTH_SHORT).show();
            Log.v("GeneratingActivity", "Playing with Wallfollower.");
            // go to play animation
            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
            setExtrasForIntent(intent);
            startActivity(intent);
            finish();
        }
    }
    private void builderFromString(String str) {
        if(str.equals("DFS")) mazeGenAlgorithm = Builder.DFS;
        if(str.equals("Prim")) mazeGenAlgorithm = Builder.Prim;
        if(str.equals("Boruvka")) mazeGenAlgorithm = Builder.Boruvka;
    }
    private void buildMazeConfig() {
        factory = new MazeFactory();
    //    seed = 13;
        percentdone = 0;
    //    if (!deterministic) {
    //        Random random = new Random();
    //        seed = random.nextInt();
    //        Log.v("GeneratingActivity", "Seed is "+seed);
    //    }
        factory.order(this);
    }
    /**
     * Sets up the onItemSelectedListener for the play type spinner.
     * Based on what play type is selected with spinner, move to correct activity since
     * progressBar has completed. Set the extras for the intent that goes to the next activity.
     * Also, put toast message and log.v output when driver is selected.
     * @return String with driver type:
     * could be Manual, Wizard, or Wallfollower
     */
    private String playTypeSpinnerSelection() {
        // code from http://www.java2s.com/Code/Android/UI/SpinnerItemSelectedListener.htm
        Spinner play_type = findViewById(R.id.play_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.play_type_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        play_type.setAdapter(adapter);
        play_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // in position one is "Manual"
                if (l == 1) {
                    driverType = "Manual";
                //    Toast.makeText(getBaseContext(), "Playing manually.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing manually.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                    finish();
                }
                // in position 2 is "Wizard"
                if (l == 2) {
                    driverType = "Wizard";
                //    Toast.makeText(getBaseContext(), "Playing with Wizard.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing with Wizard.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                    finish();
                }
                // in position 3 is "Wallfollower"
                if (l == 3) {
                    driverType = "Wallfollower";
                //    Toast.makeText(getBaseContext(), "Playing with Wallfollower.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing with Wallfollower.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return play_type.getSelectedItem().toString();
    }
    /**
     * Sets onItemSelectedListener for robot type spinner.
     * Puts toast message and log.v output with robot type selected.
     * Also, sets robotType in activity based on selected type in spinner.
     * @return String with robot type.
     * Could be: Premium, Mediocre, Soso, or Shaky.
     */
    private String getRobotType() {
        Spinner robot_type = findViewById(R.id.robot_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.robot_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robot_type.setAdapter(adapter);
        robot_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // in position 0: "Premium"
                if (l == 0) {
                    robotType = "Premium";
                //    Toast.makeText(getBaseContext(), "Premium robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Premium robot selected.");
                }
                // in position 1: "Mediocre"
                if (l == 1) {
                    robotType = "Mediocre";
                //    Toast.makeText(getBaseContext(), "Mediocre robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Mediocre robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                }
                // in position 2: "Soso"
                if (l == 2) {
                    robotType = "Soso";
                //    Toast.makeText(getBaseContext(), "Soso robot selected.",Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Soso robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                }
                // in position 3: "Shaky"
                if (l == 3) {
                    robotType = "Shaky";
                //    Toast.makeText(getBaseContext(), "Shaky robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Shaky robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    setExtrasForIntent(intent);
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return robot_type.getSelectedItem().toString();
    }
    /**
     * Private method that sets the extras that need to go to next activity
     * (PlayAnimationAcitivity or PlayManuallyActivity)
     * Puts driver type and robot type.
     * @param intent
     */
    private void setExtrasForIntent(Intent intent) {
        intent.putExtra("Driver type", driverType);
        intent.putExtra("Robot type", robotType);
        intent.putExtra("Skill level", skillLevel);
    }

    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public Builder getBuilder() {
        return mazeGenAlgorithm;
    }

    @Override
    public boolean isPerfect() {
        return perfect;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
    //    MazeSingleton.getInstance().setMaze(null);
        MazeSingleton.getInstance().setMaze(mazeConfig);
    }
    @Override
    public void updateProgress(int percentage) {
        progressBar = (ProgressBar) findViewById(R.id.loadingBar);
        progressBar.setMax(100);
        if (this.percentdone < percentage && percentage <= 100) {
            this.percentdone = percentage;
            progressBar.setProgress(percentdone, true);

        //    draw() ;
        }
    }
}

