package edu.wm.cs.cs301.janellekrupicka.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.amazebyjanellekrupicka.R;

public class AMazeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
    }
    public void exploreMaze(View view) {
        Intent intent = new Intent(this, GeneratingActivity.class);
        startActivity(intent);
    }
}