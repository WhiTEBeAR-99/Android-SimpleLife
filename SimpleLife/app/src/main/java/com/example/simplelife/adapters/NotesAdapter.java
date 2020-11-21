package com.example.simplelife.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplelife.R;
import com.example.simplelife.entities.Note;
import com.example.simplelife.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

//Setting up note adapter (extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>)
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    //TODO: Tao View holder cho adapter
    private List<Note> notes;

    //TODO: Tao handle cho View va Update Note
    private NotesListener notesListener;

    //Them NotesListener notesListener
    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.notes = notes;

        //Thao tac voi Listener
        this.notesListener = notesListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));

        //Holder cua Listener
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tTitle, tDateTime;

//        RoundedImageView imageNote;

        LinearLayout layoutNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tTitle = itemView.findViewById(R.id.textTitle);
            tDateTime = itemView.findViewById(R.id.textDateTime);
//            imageNote = itemView.findViewById(R.id.imageNote);
            layoutNote = itemView.findViewById(R.id.layoutNote);
        }

        //Goi cac attribute trong NoteDatabase
        void setNote(Note note) {
            if(note.getTitle().trim().isEmpty()) {
                tTitle.setVisibility(View.GONE);
            } else {
                tTitle.setText(note.getTitle());
            }
            tDateTime.setText(note.getDateTime());

//            if (note.getImagePath() != null) {
//                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
//                imageNote.setVisibility(View.VISIBLE);
//            } else {
//                imageNote.setVisibility(View.GONE);
//            }
        }
    }
}
