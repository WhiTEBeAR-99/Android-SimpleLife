package com.example.simplelife.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.simplelife.fragments.AskFragment;
import com.example.simplelife.fragments.CountDownFragment;
import com.example.simplelife.fragments.NoteFragment;
import com.example.simplelife.fragments.PlanFragment;
import com.example.simplelife.R;
import com.example.simplelife.fragments.SettingFragment;
import com.example.simplelife.fragments.TodoListFragment;

public class MenuActivity extends AppCompatActivity {

    ImageButton btnAsk, btnCountDown, btnNote, btnPlan, btnSetting, btnTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Set full screen (Hide status bar)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set full screen (Hide title bar/action bar)
        try
        {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        //Edit code
        btnAsk = (ImageButton) findViewById(R.id.ask_button);
        btnCountDown = (ImageButton) findViewById(R.id.countdown_anniversary_button);
        btnNote = (ImageButton) findViewById(R.id.note_button);
        btnPlan = (ImageButton) findViewById(R.id.calender_button);
        btnSetting = (ImageButton) findViewById(R.id.setting_button);
        btnTodoList = (ImageButton) findViewById(R.id.todo_button);

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AskFragment fragment = new AskFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });
        btnCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CountDownFragment fragment = new CountDownFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                NoteFragment fragment = new NoteFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });
        btnPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PlanFragment fragment = new PlanFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SettingFragment fragment = new SettingFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });
        btnTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                TodoListFragment fragment = new TodoListFragment();
                fm.beginTransaction()
                        .replace(R.id.container_fragment, fragment)
                        .commit();
            }
        });

        //Log ra console cua Dev
        Log.d("MY_MENU", "Menu app is showing... wait for user choosen");
    }

    //Vi trong fragmentNote khong goi duoc onActivityResult nen se goi o main parent,
    //roi sau do tu cac fragment co the goi duoc method nay
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}