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

public class LosingActivity extends AppCompatActivity {
    private int pathLengthInt;
    private int shortestPath;
    private boolean crashed;
    private int energyConsumed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        Intent intent = getIntent();
        if(null!=intent) {
            pathLengthInt = intent.getIntExtra("Path length", 500); // 500 for testing
            shortestPath = intent.getIntExtra("Shortest path length", 500);
            crashed = intent.getBooleanExtra("Reason for stop", true);
            energyConsumed = intent.getIntExtra("Energy consumption", 500);
        }
        setText();
    }
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

}