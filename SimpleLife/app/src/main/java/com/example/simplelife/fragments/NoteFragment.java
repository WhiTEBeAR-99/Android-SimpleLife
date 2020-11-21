package com.example.simplelife.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.simplelife.R;
import com.example.simplelife.activities.NewNoteActivity;
import com.example.simplelife.adapters.NotesAdapter;
import com.example.simplelife.database.NotesDatabase;
import com.example.simplelife.entities.Note;
import com.example.simplelife.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

    //Them implement tu Listener
public class NoteFragment extends Fragment implements NotesListener{

    public static final int REQUEST_CODE_ADD_NOTE = 1; //use to add new note
    public static final int REQUEST_CODE_UPDATE_NOTE = 2; //use to update note
    public static final int REQUEST_CODE_SHOW_NOTES = 3; //use to shoe all notes

    ImageButton btnNew;
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;

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
        //TODO: Load NoteDatabase
        notesRecyclerView = v.findViewById(R.id.note_recyclerview);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES, false);
        //CODE: call from onCreate of NoteFragment. Display note from database and check is it load successfull with REQUEST_CODE_SHOW_NOTES
        //fasle: because we are display note from database so the "isNoteDeleted"=false

        //TODO: Chuc nang Create New Note
        btnNew = v.findViewById(R.id.new_button);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), NewNoteActivity.class));
                startActivityForResult(
                        new Intent(getActivity(), NewNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
        //Your code end here
        return v;
    }

    //TODO: Xu ly hien thi note
    private  void getNotes(final int requestCode, final boolean isNoteDeleted) { //add new var isNoteDelete

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
//                if (noteList.size() == 0) { //nghia la chua co note nao duoc load tu database len recylerview ca.
//                    noteList.addAll(notes); //do do ta load toan bo note co trong database len
//                    notesAdapter.notifyDataSetChanged(); //thong bao ben adapter rang ta da load len
//                } else { //neu size khong rong, co nghia la da co note ben recylerview
//                    noteList.add(0, notes.get(0)); //load note moi nhat len recylcerview
//                    notesAdapter.notifyDataSetChanged();
//                }
//                notesRecyclerView.smoothScrollToPosition(0); //xem recylerview tu dau

                //Lock code from line 118 to line 125
                //ViewOrUpdate Note
                if (requestCode == REQUEST_CODE_SHOW_NOTES) { //use REQUEST_CODE_SHOW_NOTES, so we adding all notes from database to noteList and notify adapter about the new dataset
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) { //use REQUEST_CODE_ADD_NOTE, so we can adding an only note (newly added note) from the  database to noteList and notify the adapter for the newly inserted item and scrolling recycler view to the top.
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) { //use REQUEST_CODE_UPDATE_NOTE, so we removing note from the clicked position and adding the lasted updated note from same position from the database and notify the adapter for item changed at the position
                    noteList.remove(noteClickedPosition);
//                    noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
//                    notesAdapter.notifyItemChanged(noteClickedPosition);
                      //Move to line 147

                    //For isNoteDeleted
                    if (isNoteDeleted) { //for meaning from line 138 to line 151. If the code is REQUEST_CODE_UPDATE_NOTE. First the note will be deleted from list. Then checked whether the note is deleted or not. If the note is deleted then notify adapter about the item removed. If the note is not deleted then it must be updated. So we are adding a newly updated note to the same position where we removed and notifying adapter about item changed.
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    }else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }

                //Log ra console cua Dev
                Log.d("MY_NOTES", "NoteDatabase: " + notes.toString());
            }
        }
        new GetNoteTask().execute();
    }

    //Dung de add ngay lap tuc note vua moi tao vao UI cua NoteFragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_SHOW_NOTES, false);
            //call from onActivityResult. Check is the new note add from NewNoteActivity and its result is sent back to this NoteFragment.
            //false: because we are adding a new note to database, so the "isNoteDeleted"=falsd
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) { //Add new in ViewOrUpdate Note
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
                //call from onActivityResult. Check is the already note is updated from NewNoteActivity and its result is sent back to thus NoteFragment.
                //false: because we are updating a note available from database, may be we update, or delete note so the default is false. And we passing value from NewNoteActivity with the key "isNoteDeleted"
            }
        }
    }

    //Listener View - Update Note
    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getActivity(), NewNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }
}