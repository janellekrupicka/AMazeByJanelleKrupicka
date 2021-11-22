package edu.wm.cs.cs301.janellekrupicka.gui;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amazebyjanellekrupicka.R;
import com.google.android.material.snackbar.Snackbar;

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

        progress = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
                        if(selectedPlay.equals("None selected")) {
                            Toast.makeText(getBaseContext(), "Select driver to continue.", LENGTH_LONG).show();
                            playTypeSpinnerSelection();
                        }
                        if(selectedPlay.equals("Manual")) {
                        //    Toast.makeText(getBaseContext(), "Playing manually.", LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                            startActivity(intent);
                        }
                        if (selectedPlay.equals("Wizard")) {
                        //    Toast.makeText(getBaseContext(), "Playing with Wizard.", LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                            startActivity(intent);
                        }
                        if (selectedPlay.equals("Wallfollower")) {
                        //    Toast.makeText(getBaseContext(), "Playing with Wallfollower.", LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                            startActivity(intent);
                        }
                        // ---0 - VISIBLE; 4 - INVISIBLE; 8 - GONE---
                        //    progressBar.setVisibility(View.GONE);


                        // String selectedPlay = play_type.getItemAtPosition(play_type.getSelectedItemPosition()).toString();
                        // if(selectedPlay.equals("Manual")) {
                        //   Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                        // startActivity(intent);
                        //}
                        //if (selectedPlay.equals("Wizard") || selectedPlay.equals("Wallfollower")) {
                        //  Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                        //startActivity(intent);
                        // }
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
    private void playTypeSpinnerSelection() {
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
                  //  Toast.makeText(getBaseContext(), "Playing manually.", LENGTH_LONG).show();
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayManuallyActivity.class);
                    startActivity(intent);
                }
                if (l == 2) {
                  //  Toast.makeText(getBaseContext(), "Playing with Wizard.", LENGTH_LONG).show();
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
                if (l == 3) {
                  //  Toast.makeText(getBaseContext(), "Playing with Wallfollower.", LENGTH_LONG).show();
                    Intent intent = new Intent(GeneratingActivity.this.getBaseContext(), PlayAnimationActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getBaseContext(), "Nothing selected.", LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}

