package com.example.simplelife.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplelife.R;
import com.example.simplelife.database.NotesDatabase;
import com.example.simplelife.entities.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends AppCompatActivity {

    ImageButton btnBack, btnSave;

    EditText etTitleNote, etNoteText;

    TextView tvDateTime;
    Calendar currentTime;
    SimpleDateFormat simpleDateFormat;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        //Set full screen (Hide status bar)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set full screen (Hide title bar/action bar)
        try
        {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        //Log ra console cua Dev
        Log.d("MY_NOTES", "NewNoteActivity is opening...");

        //Your code here
        etTitleNote = (EditText) findViewById(R.id.title_et);
        etNoteText = (EditText) findViewById(R.id.content_et);

        //TODO: Chuc nang cua btnBack
        btnBack = (ImageButton) findViewById(R.id.back_button);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(NewNoteActivity.this, MenuActivity.class);
                //startActivity(intent);
                onBackPressed(); //Dung thay cho 2 dong tren
                //Log ra console cua Dev
                Log.d("MY_NOTES", "Back to NoteFragment succesful");
            }
        });

        //TODO: Chuc nang cua btnSave
        btnSave = (ImageButton) findViewById(R.id.save_button);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        //TODO: Xu ly get DateTime
        tvDateTime = (TextView) findViewById(R.id.get_date_time_tv);
        //Lay thoi gian he thong
        currentTime = Calendar.getInstance();
        //Doi dinh dang thoi gian
        simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm:ss a", Locale.getDefault());
        Date = simpleDateFormat.format(currentTime.getTime());
        //Gan thoi gian cho TextView
        tvDateTime.setText(Date);

        //Log ra console cua Dev
        Log.d("MY_NOTES", "Current time when open NewNoteActivity " + Date);

    }

    //TODO: Xu ly save note
    private void saveNote() {
        if (etNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Chưa có nội dung", Toast.LENGTH_SHORT).show();
            //Log ra console cua Dev
            Log.d("MY_NOTES", "The content is empty");
            return;
        }

        //Chuan bi goi database Note
        final Note note = new Note();
        note.setTitle(etTitleNote.getText().toString());
        note.setNoteText(etNoteText.getText().toString());
        note.setDateTime(tvDateTime.getText().toString());

        //Luu vao Room Database bang async method
        @SuppressLint("StaticFieldLeak")
        class SaveNotTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNotTask().execute();

        //Log ra console cua Dev
        Log.d("MY_NOTES", "Save successfull");

    }
}