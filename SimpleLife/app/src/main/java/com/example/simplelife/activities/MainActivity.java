package com.example.simplelife.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.simplelife.R;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout layoutBegin;
    TextView tvClickToContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set full screen (Hide status bar)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set full screen (Hide title bar/action bar)
        try
        {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        //Log ra console cua Dev
        Log.d("MY_MAIN", "App is running...");

        //Edit code
        layoutBegin = (ConstraintLayout) findViewById(R.id.layout_begin);
        layoutBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(myIntent);
            }
        });

        tvClickToContinue = (TextView) findViewById(R.id.click_to_continue);
        tvClickToContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(myIntent);
            }
        });

        //Log ra console cua Dev
        Log.d("MY_MAIN", "Fragment menu is ready. Click anywhere in screen to continue.");
    }
}