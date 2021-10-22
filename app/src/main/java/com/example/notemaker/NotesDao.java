package com.example.notemaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDao extends SQLiteOpenHelper {

    public static final String COL_NOTE = "NOTE";
    public static final String NOTES_TABLE = COL_NOTE + "S_TABLE";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_SUBTITLE = "SUBTITLE";
    public static final String COL_CREATED_ON = "CREATED_ON";
    public static final String COL_UPDATED_ON = "UPDATED_ON";
    public static final String COL_ID = "ID";

    public NotesDao(@Nullable Context context) {
        super(context, "notes", null, 1);
    }

    //create db and tables here
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStmt = "CREATE TABLE " + NOTES_TABLE + " (" + COL_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT NOT NULL, " + COL_SUBTITLE +
                " TEXT, " + COL_NOTE + " TEXT, " + COL_CREATED_ON + " TEXT, " + COL_UPDATED_ON + " TEXT)";

        db.execSQL(createTableStmt);
    }

    //ignore; it's just required by the class
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //create
    public long createNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_TITLE, note.getTitle());
        cv.put(COL_SUBTITLE, note.getSubtitle());
        cv.put(COL_NOTE, note.getNote());
        cv.put(COL_CREATED_ON, note.getCreated());
        cv.put(COL_UPDATED_ON, note.getUpdated());

        long success = db.insert(NOTES_TABLE, null, cv);
        return success;
    }

    //read
    public NotesModel[] getAllNotes() {
        String slct = "SELECT * FROM " + NOTES_TABLE + ";";

        return null;
    }

    public NotesModel[] getNotesByTitleSearch(String title) {
        return null;
    }

    public NotesModel getNoteById(int id) {
        return null;
    }

    //update
    public void updateNoteById(NotesModel note){

    }

    //delete
    public void deleteNoteById(int id) {}
}
