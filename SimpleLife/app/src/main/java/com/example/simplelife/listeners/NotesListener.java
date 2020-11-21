package com.example.simplelife.listeners;

import com.example.simplelife.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
