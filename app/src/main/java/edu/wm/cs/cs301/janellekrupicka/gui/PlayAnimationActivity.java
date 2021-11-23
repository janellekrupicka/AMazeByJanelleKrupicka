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

public class PlayAnimationActivity extends AppCompatActivity {
    private String driverType;
    private String robotType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
        Intent intent = getIntent();
        driverType=intent.getStringExtra("Driver type");
        robotType=intent.getStringExtra("Robot type");
        getAnimationSpeed();
        showMap();
    }
    public void go2Winning(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        //intent.putExtra("Robot", true);
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        intent.putExtra("Energy consumption", 0); // will get from controller
        startActivity(intent);
    }
    public void go2Losing(View view) {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("Path length", 0); // will get from controller
        intent.putExtra("Shortest path length", 0); // will get from controller
        intent.putExtra("Energy consumption", 0); // will get from controller
        intent.putExtra("Reason for stop", true);// true if crashed/broke, false if ran out of energy
        startActivity(intent);
    }
    public void increaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale increased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale increased");
    }
    public void decreaseMapScale(View view) {
        Toast.makeText(getBaseContext(), "Map scale decreased", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Map scale decreased");
    }
    public void pressPlay(View view) {
        Toast.makeText(getBaseContext(), "Playing animation", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Playing animation");
    }
    public void pressPause(View view) {
        Toast.makeText(getBaseContext(), "Animation paused", Toast.LENGTH_SHORT).show();
        Log.v("PlayAnimationActivity", "Animation paused");
    }
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
    private void setEnergyLevel(int energy) {
        ProgressBar energyLevel=(ProgressBar) findViewById(R.id.energyBar);
        energyLevel.setProgress(energy);
    }
}