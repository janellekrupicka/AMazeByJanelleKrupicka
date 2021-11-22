package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class AMazeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        getMazeGenAlg();
        getSkillLevel();
        getRooms();
    }
    public void exploreMaze(View view) {
        Toast.makeText(getBaseContext(), "Exploring maze.", LENGTH_LONG).show();
        Log.v("AMazeActivity", "Explore button selected. Exploring maze.");
        Intent intent = new Intent(this, GeneratingActivity.class);
        startActivity(intent);
    }
    public void revisitMaze(View view) {
        Toast.makeText(getBaseContext(), "Revisiting maze.", LENGTH_LONG).show();
        Log.v("AMazeActivity", "Revisit button selected. Revisiting maze.");
        Intent intent = new Intent(this, GeneratingActivity.class);
        startActivity(intent);
    }
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
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getBaseContext(), "Skill level: "+skillLevel.getProgress(), LENGTH_LONG).show();
                Log.v("AMazeActivity", "Skill level selected: "+skillLevel.getProgress());
            }
        });
        return skillLevel.getProgress();
    }
    private boolean getRooms() {
        Switch rooms=(Switch) findViewById(R.id.rooms);
        // code from: https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
        rooms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getBaseContext(), "Generating maze with rooms", LENGTH_LONG).show();
                    Log.v("AMazeActivity", "Generating maze with rooms");
                }
                if(!isChecked) {
                    Toast.makeText(getBaseContext(), "Generating maze without rooms", LENGTH_LONG).show();
                    Log.v("AMazeActivity", "Generating maze without rooms");
                }
            }
        });
        return rooms.isChecked();
    }
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
                        Toast.makeText(getBaseContext(), "Generating maze with DFS", LENGTH_LONG).show();
                        Log.v("AMazeActivity", "Maze generation algorithm selected: DFS");
                    }
                    if(i==1) {
                        Toast.makeText(getBaseContext(), "Generating maze with Prim", LENGTH_LONG).show();
                        Log.v("AMazeActivity", "Maze generation algorithm selected: Prim");
                    }
                    if(i==2) {
                        Toast.makeText(getBaseContext(), "Generating maze with Boruvka", LENGTH_LONG).show();
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