package com.example.simplelife.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplelife.R;
import com.example.simplelife.database.NotesDatabase;
import com.example.simplelife.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
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

//    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
//    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
//    ImageView imgNote;
//    private String selectedImagePath;

    //DeleteNote
    LinearLayout layoutDeleteNote;
    private AlertDialog dialogDeleteNote;

    //ViewOrUpdateNote
    private Note alreadyAvailableNote;

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

        //ViewOrUpdate Note
        if (getIntent().getBooleanExtra("isViewOrUpdate", false)){
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        //TODO: Xu ly DeleteNote
        layoutDeleteNote = (LinearLayout) findViewById(R.id.layoutDeleteNote);
        //Check if this is null mean this note is creating. Not for viewing or updating
        if (alreadyAvailableNote != null) {
            layoutDeleteNote.setVisibility(View.VISIBLE); //this note is created before and now is viewing/updating
            layoutDeleteNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteNoteDialog();
                }
            });
        }

        //Goi Miscellaneous
//        initMiscellaneous();

        //TODO: Xu ly btn AddImage
//        imgNote = (ImageView) findViewById(R.id.imageNote);
//        selectedImagePath = "";

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
//        note.setImagePath(selectedImagePath);

        //ViewOrUpdate Note
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId()); //Set old note with update will have new id
        }

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

    //TODO: Xu ly View - Update Note
    private void setViewOrUpdateNote() {
        etTitleNote.setText(alreadyAvailableNote.getTitle());
        etNoteText.setText(alreadyAvailableNote.getNoteText());
        tvDateTime.setText(alreadyAvailableNote.getDateTime());
    }

    //TODO: Su kien OnClickListner vao text cua Khac
//    private void initMiscellaneous() {
//        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
//        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
//        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                } else {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//        });
//
//        //Check if this is null mean this note is creating. Not for viewing or updating
//        if (alreadyAvailableNote != null) {
//            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE); //this note is created before and now is viewing/updating
//            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    showDeleteNoteDialog();
//                }
//            });
//        }
//    }

    //TODO: Hien thi AlertDeleteNote
    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NewNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            //Xac nhan Delete Note
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            //Huy Delete
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }
//
//        //TODO: Xu li btnAddImage vao note trong Miscellaneous
//        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                if (ContextCompat.
//                        checkSelfPermission(getApplicationContext(),
//                                Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(
//                            NewNoteActivity.this,
//                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//                            REQUEST_CODE_STORAGE_PERMISSION
//                    );
//                } else {
//                    selectImage();
//                }
//            }
//        });
//    }

    //Chon Image trong storage
//    private void selectImage() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
//        }
//    }

    //Goi status xin cap quyen READ_EXTERNAL_STORAGE
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                selectImage();
//            } else {
//                Toast.makeText(this, "Chưa được cấp quyền", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    //Quan ly ket qua su kien khi da chon duoc image
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri selectedImageUri = data.getData();
//                if (selectedImageUri != null) {
//                    try {
//                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
//                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                        imgNote.setImageBitmap(bitmap);
//                        imgNote.setVisibility(View.VISIBLE);
//
//                        selectedImagePath = getPathFromUri(selectedImageUri);
//
//                    } catch (Exception exception) {
//                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    }

//    private String getPathFromUri(Uri contentUri) {
//        String filePath;
//        Cursor cursor = getContentResolver()
//                .query(contentUri, null, null, null, null);
//        if (cursor == null) {
//            filePath = contentUri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int index = cursor.getColumnIndex("_data");
//            filePath = cursor.getString(index);
//            cursor.close();
//        }
//        return filePath;
//    }
}