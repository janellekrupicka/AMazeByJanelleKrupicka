package edu.wm.cs.cs301.janellekrupicka.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    public void shortCut(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        startActivity(intent);
    }
}