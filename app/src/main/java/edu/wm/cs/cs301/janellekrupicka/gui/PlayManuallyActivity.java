package edu.wm.cs.cs301.janellekrupicka.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.amazebyjanellekrupicka.R;

public class PlayManuallyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}