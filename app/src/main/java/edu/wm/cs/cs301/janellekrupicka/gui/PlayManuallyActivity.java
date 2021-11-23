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

public class PlayManuallyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        showMap();
        showSolution();
        showVisibleWalls();
    }
    public void shortCut(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        startActivity(intent);
    }
    public void increaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale increased", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Map scale increase selected");
    }
    public void decreaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale decreased", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Map scale decrease selected");
    }
    public void moveForward(View view) {
        Toast.makeText(getBaseContext(), "Move forward", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Forward button selected");
    }
    public void turnLeft(View view) {
        Toast.makeText(getBaseContext(), "Turn left", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Left button selected");
    }
    public void turnRight(View view) {
        Toast.makeText(getBaseContext(), "Turn right", Toast.LENGTH_SHORT).show();
        Log.v("PlayManuallyActivity", "Right button selected");
    }
    private void showMap() {
        Switch showMap=(Switch) findViewById(R.id.show_map);
        // code for onCheckedChangeListener from
        // https://stackoverflow.com/questions/11278507/android-widget-switch-on-off-event-listener
        showMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getBaseContext(), "Showing map", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show map turned on");
                }
                if(!isChecked) {
                    Toast.makeText(getBaseContext(), "Not showing map", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show map turned off");
                }
            }
        });
    }
    private void showSolution() {
        Switch showSolution=(Switch) findViewById(R.id.show_solution);
        showSolution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getBaseContext(), "Showing solution", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show map turned on");
                }
                if(!isChecked) {
                    Toast.makeText(getBaseContext(), "Not showing solution", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show solution turned off");
                }
            }
        });
    }
    private void showVisibleWalls() {
        Switch showVisibleWalls=(Switch) findViewById(R.id.show_walls);
        showVisibleWalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getBaseContext(), "Showing visible walls", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show visible walls turned on");
                }
                if(!isChecked) {
                    Toast.makeText(getBaseContext(), "Not showing visible walls", Toast.LENGTH_SHORT).show();
                    Log.v("PlayManuallyActivity", "Show visible walls turned off");
                }
            }
        });
    }


}