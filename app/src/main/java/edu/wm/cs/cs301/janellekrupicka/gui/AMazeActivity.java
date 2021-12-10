package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;

import java.util.Random;

/**
 *Initial activity state for the UI.
 * Sets the maze difficulty, whether the maze is built with rooms,
 * the algorithm used to generate the maze, and moves to play the maze.
 *
 * Collaborators:
 * Controller.java to set maze with rooms, builder
 * StateGenerating.java to set the skill level
 * GeneratingActivity.java moved to when explore or revisit is selected
 */
public class AMazeActivity extends AppCompatActivity {
    /**
     * Maze skill level. AKA maze size, maze difficulty.
     */
    private int skillLevelInt;
    /**
     * Whether the maze will be set with maze.
     * True if rooms, false if no rooms.
     */
    private boolean hasRooms;
    /**
     * Maze generation algorithm.
     * DFS, Prim, or Boruvka
     */
    private String mazeGenAlgorithm;
    private int seed;
    /**
     * Method called when AMazeActivity is created.
     * Sets the layout, the default generation algorithm,
     * the default skill level, the default boolean for rooms.
     * (Also starts the on item selected listeners for
     * - maze generation algorithm spinner
     * - skill level seek bar
     * - rooms switch)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        mazeGenAlgorithm=getMazeGenAlg();
        skillLevelInt=getSkillLevel();
        hasRooms=getRooms();
    }
    @Override
    protected void onPause() {
        Log.v("AMazeActivity", "In onPause");
        setUpPreferences();
        super.onPause();
    }
    private void determineSeed() {
        Random random = new Random();
        seed = random.nextInt();
    //    seed = -1536304652;
        Log.v("AMazeActivity", "seed is "+seed);
    }
    /**
     * Method called with explore button is selected.
     * Toast message and log.v output shown when button selected.
     * Creates new intent for GeneratingActivity and
     * moves to to GeneratingActivity.
     * Before moving, sets extras for the inten to GeneratingActivity
     * to skill level, rooms, and maze generation algorithm so that
     * those values can be accessed in GeneratingActivity.
     * @param view
     */
    public void exploreMaze(View view) {
    //    Toast.makeText(getBaseContext(), "Exploring maze.", LENGTH_LONG).show();
        Log.v("AMazeActivity", "Explore button selected. Exploring maze.");
        Intent intent = new Intent(this, GeneratingActivity.class);
        Log.v("AMazeActivity", "Skill level:"+skillLevelInt);
        intent.putExtra("Skill level",skillLevelInt);
        intent.putExtra("Rooms", hasRooms);
        intent.putExtra("Maze gen algorithm",mazeGenAlgorithm);
        determineSeed();
        intent.putExtra("Seed", seed);
    //    setUpPreferences();
        startActivity(intent);
    }
    /**
     * Method called when revisit button is selected.
     * Toast message and log.v output shown when button is selected.
     * Will ultimately set skillLevel, hasRooms, and mazeGenAlgorithm
     * from persistent storage along with the seed from the previous
     * maze created. (Will revist previous maze)
     * Creates an intent to go to GeneratingActivity with
     * skill level, rooms, and maze gen algorithm as extras.
     * @param view
     */
    public void revisitMaze(View view) {
    //    Toast.makeText(getBaseContext(), "Revisiting maze.", LENGTH_LONG).show();
        Log.v("AMazeActivity", "Revisit button selected. Revisiting maze.");
        Intent intent = new Intent(this, GeneratingActivity.class);
        getFromPreferences();
        intent.putExtra("Skill level",skillLevelInt);
        intent.putExtra("Rooms", hasRooms);
        intent.putExtra("Maze gen algorithm",mazeGenAlgorithm);
        intent.putExtra("Seed", seed);
    //    setUpPreferences();
        startActivity(intent);
    }
    private void setUpPreferences() {
        Log.v("AMazeActivity", "In setUpPreferences");
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Seed", seed);
        editor.putBoolean("Rooms", hasRooms);
        editor.putInt("Skill level", skillLevelInt);
        editor.putString("Maze gen algorithm", mazeGenAlgorithm);
        editor.apply();
    }
    private void getFromPreferences() {
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        seed = sharedPref.getInt("Seed", 13);
        hasRooms = sharedPref.getBoolean("Rooms", true);
        skillLevelInt = sharedPref.getInt("Skill level", 0);
        mazeGenAlgorithm = sharedPref.getString("Maze gen algorithm", "DFS");
    }

    /**
     * Starts the onSeekBarChangeListener for skill level seekbar.
     * When seek bar is released/set, puts toast message and log.v output
     * with the selected skill level.
     * @return skill level int (between 0-9)
     */
    private int getSkillLevel() {
        // return an int from 0 to 9 for skill level
        SeekBar skillLevel=(SeekBar) findViewById(R.id.skill_level);
        // code from: http://www.javased.com/index.php?api=android.widget.SeekBar.OnSeekBarChangeListener
        skillLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            /**
             * When SeekBar thumb is released,
             * the user is finished setting the skill level.
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // sets instance variable to SeekBar progress
                skillLevelInt = skillLevel.getProgress();
                Toast.makeText(getBaseContext(), "Skill level: "+skillLevel.getProgress(), LENGTH_LONG).show();
                Log.v("AMazeActivity", "Skill level selected: "+skillLevel.getProgress());
            }
        });
        return skillLevel.getProgress();
    }
    /**
     * Sets up the onCheckedChangeListener for the rooms switch
     * that sets the the rooms instance variable based on if the
     * switch is toggled on.
     * Puts up a toast message and log.v output when the switch is
     * turned on or off.
     * @return boolean: true if rooms, false if no rooms
     */
    private boolean getRooms() {
        Switch rooms=(Switch) findViewById(R.id.rooms);
        // code from: https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
        rooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    hasRooms = true;
                //    Toast.makeText(getBaseContext(), "Generating maze with rooms", LENGTH_LONG).show();
                    Log.v("AMazeActivity", "Generating maze with rooms");
                }
                if(!isChecked) {
                    hasRooms = false;
                //    Toast.makeText(getBaseContext(), "Generating maze without rooms", LENGTH_LONG).show();
                    Log.v("AMazeActivity", "Generating maze without rooms");
                }
            }
        });
        return rooms.isChecked();
    }
    /**
     * Sets up the onItemSelectedListener for the
     * maze generation algorithm spinner.
     * Outputs a toast message and log.v output depending on
     * what maze gen algorithm is selected.
     * @return String, maze generation algorithm
     * DFS, Prim, or Boruvka
     */
    private String getMazeGenAlg() {
            // code from http://www.java2s.com/Code/Android/UI/SpinnerItemSelectedListener.htm
            Spinner mazeGenAlg = findViewById(R.id.maze_gen_alg);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.maze_generation_alg,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mazeGenAlg.setAdapter(adapter);
            mazeGenAlg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==0) {
                        mazeGenAlgorithm="DFS";
                    //    Toast.makeText(getBaseContext(), "Generating maze with DFS", LENGTH_LONG).show();
                        Log.v("AMazeActivity", "Maze generation algorithm selected: DFS");
                    }
                    if(i==1) {
                        mazeGenAlgorithm="Prim";
                    //    Toast.makeText(getBaseContext(), "Generating maze with Prim", LENGTH_LONG).show();
                        Log.v("AMazeActivity", "Maze generation algorithm selected: Prim");
                    }
                    if(i==2) {
                        mazeGenAlgorithm="Boruvka";
                    //    Toast.makeText(getBaseContext(), "Generating maze with Boruvka", LENGTH_LONG).show();
                        Log.v("AMazeActivity", "Maze generation algorithm selected: Boruvka");
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        return mazeGenAlg.getSelectedItem().toString();
    }
}