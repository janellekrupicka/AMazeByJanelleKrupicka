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

public class GeneratingActivity extends AppCompatActivity {

    // code for background thread from
    // http://www.java2s.com/Code/Android/UI/UsingThreadandProgressbar.htm
    private static int progress;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        getRobotType();

        progress = 0;
        progressBar = (ProgressBar) findViewById(R.id.loadingBar);
        progressBar.setMax(200);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 200) {
                    progressStatus = doSomeWork();
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    public void run() {
                        Spinner play_type = findViewById(R.id.play_type);
                        String selectedPlay = play_type.getItemAtPosition(play_type.getSelectedItemPosition()).toString();
                        if (selectedPlay.equals("None selected")) {
                            Toast.makeText(getBaseContext(), "Select driver to continue.", LENGTH_LONG).show();
                            playTypeSpinnerSelection();
                        }
                        else if (selectedPlay.equals("Manual")) {
                            Toast.makeText(getBaseContext(), "Playing manually.", Toast.LENGTH_SHORT).show();
                            Log.v("GeneratingActivity", "Playing manually.");
                            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                            startActivity(intent);
                        }
                        else if (selectedPlay.equals("Wizard")) {
                            Toast.makeText(getBaseContext(), "Playing with Wizard.", Toast.LENGTH_SHORT).show();
                            Log.v("GeneratingActivity", "Playing with Wizard.");
                            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                            startActivity(intent);
                        }
                        else if (selectedPlay.equals("Wallfollower")) {
                            Toast.makeText(getBaseContext(), "Playing with Wallfollower.", Toast.LENGTH_SHORT).show();
                            Log.v("GeneratingActivity", "Playing with Wallfollower.");
                            Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            private int doSomeWork() {
                try {
                    // ---simulate doing some work---
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();
    }
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
              //  if (l == 0) {
                //    Toast.makeText(getBaseContext(), "Select driver to continue.", LENGTH_LONG).show();
              //  }
                if (l == 1) {
                    Toast.makeText(getBaseContext(), "Playing manually.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing manually.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                    startActivity(intent);
                }
                if (l == 2) {
                    Toast.makeText(getBaseContext(), "Playing with Wizard.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing with Wizard.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
                if (l == 3) {
                    Toast.makeText(getBaseContext(), "Playing with Wallfollower.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Playing with Wallfollower.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return play_type.getSelectedItem().toString();
    }
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
                if (l == 0) {
                    Toast.makeText(getBaseContext(), "Premium robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Premium robot selected.");
                }
                if (l == 1) {
                    Toast.makeText(getBaseContext(), "Mediocre robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Mediocre robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                    startActivity(intent);
                }
                if (l == 2) {
                    Toast.makeText(getBaseContext(), "Soso robot selected.",Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Soso robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
                if (l == 3) {
                    Toast.makeText(getBaseContext(), "Shaky robot selected.", Toast.LENGTH_SHORT).show();
                    Log.v("GeneratingActivity", "Shaky robot selected.");
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        return robot_type.getSelectedItem().toString();
    }
}

