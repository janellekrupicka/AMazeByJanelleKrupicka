package edu.wm.cs.cs301.janellekrupicka.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amazebyjanellekrupicka.R;

public class PlayAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
    }

    public void go2Winning(View view) {
        Intent intent = new Intent(this, WinningActivity.class);
        startActivity(intent);
    }
    public void go2Losing(View view) {
        Intent intent = new Intent(this, LosingActivity.class);
        startActivity(intent);
    }

}