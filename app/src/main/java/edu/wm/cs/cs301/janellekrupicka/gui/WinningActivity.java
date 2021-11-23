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

public class WinningActivity extends AppCompatActivity {
    private int pathLength;
    private int shortestPath;
    private boolean manual;
    private int energyConsumed;
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
