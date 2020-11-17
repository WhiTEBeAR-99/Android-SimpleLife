package com.example.simplelife.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.simplelife.R;
import com.example.simplelife.activities.NewNoteActivity;
import com.example.simplelife.database.NotesDatabase;
import com.example.simplelife.entities.Note;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    ImageButton btnNew;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log ra console cua Dev
        Log.d("MY_NOTE", "NoteFragment is opening...");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_note, container, false);

        //Your code start here
        //TODO: Chuc nang Create New Note
        btnNew = v.findViewById(R.id.new_button);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), NewNoteActivity.class));
                startActivityForResult(
                        new Intent(getActivity(), NewNoteActivity.class), REQUEST_CODE_ADD_NOTE
                );
            }
        });

        //TODO: Show note da tao
        getNote();

        //Your code end here
        return v;
    }

    //TODO: Xu ly hien thi note
    private  void getNote() {

        //Vi ben SaveNotTask da dung async method nen ben GetNoteTask cung phai dung async method
        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>> {
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase
                        .getDatabase(getActivity())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                //Log ra console cua Dev
                Log.d("MY_NOTES", "NoteDatabase: " + notes.toString());
            }
        }
        new GetNoteTask().execute();
    }
}