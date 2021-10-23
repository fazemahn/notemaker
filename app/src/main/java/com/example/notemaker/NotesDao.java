package com.example.notemaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotesDao extends SQLiteOpenHelper {
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String COL_NOTE = "NOTE";
    public static final String NOTES_TABLE = COL_NOTE + "S_TABLE";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_SUBTITLE = "SUBTITLE";
    public static final String COL_COLOUR = "COLOUR";
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
                " TEXT, " + COL_NOTE + " TEXT, " + COL_COLOUR + " TEXT, " + COL_CREATED_ON
                + " TEXT, " + COL_UPDATED_ON + " TEXT)";

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
        cv.put(COL_COLOUR, note.getColour());
        cv.put(COL_CREATED_ON, note.getCreated());
        cv.put(COL_UPDATED_ON, note.getUpdated());

        long success = db.insert(NOTES_TABLE, null, cv);
        db.close();
        return success;
    }

    //read
    public List<NotesModel> getAllNotes() {
        List<NotesModel> notes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE;

        Cursor cur = db.rawQuery(query, null);

        if(cur.moveToFirst()) {
            do {
                //get row data
                int id = cur.getInt(0);
                String title = cur.getString(1);
                String subtitle = cur.getString(2);
                String note = cur.getString(3);
                String colour = cur.getString(4);
                String crt = cur.getString(5);
                String upd = cur.getString(6);

                //convert date string to date obj
                LocalDateTime created = LocalDateTime.parse(crt, df);
                LocalDateTime updated = LocalDateTime.parse(upd, df);

                //make note and add to list
                NotesModel n = new NotesModel(id, title, subtitle, note, colour, created, updated);
                notes.add(n);
            } while(cur.moveToNext());
        }

        db.close();
        cur.close();
        return notes;
    }

    public List<NotesModel> getNotesByTitleSearch(String title) {
        List<NotesModel> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " + COL_TITLE + " LIKE %" + title + "%";
        Cursor cur =  db.rawQuery(query, null);

        if(cur.moveToFirst()) {
            do {
                //get row data
                int id = cur.getInt(0);
                String titleDb = cur.getString(1);
                String subtitle = cur.getString(2);
                String note = cur.getString(3);
                String colour = cur.getString(4);
                String crt = cur.getString(5);
                String upd = cur.getString(6);

                //convert date string to date obj
                LocalDateTime created = LocalDateTime.parse(crt, df);
                LocalDateTime updated = LocalDateTime.parse(upd, df);

                //make note and add to list
                NotesModel n = new NotesModel(id, titleDb, subtitle, note, colour, created, updated);
                notes.add(n);
            } while(cur.moveToNext());
        }

        db.close();
        cur.close();
        return notes;
    }

    public NotesModel getNoteById(NotesModel note) {
        SQLiteDatabase db = this.getReadableDatabase();
        NotesModel n = null;
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " + COL_ID + " = " + note.getId();

        Cursor cur = db.rawQuery(query, null);

        if(cur.moveToFirst()){
            //get row data
            int id = cur.getInt(0);
            String title = cur.getString(1);
            String subtitle = cur.getString(2);
            String msg = cur.getString(3);
            String colour = cur.getString(4);
            String crt = cur.getString(5);
            String upd = cur.getString(6);

            //convert date string to date obj
            LocalDateTime created = LocalDateTime.parse(crt, df);
            LocalDateTime updated = LocalDateTime.parse(upd, df);

            //make note and add to list
            n = new NotesModel(id, title, subtitle, msg, colour, created, updated);
        }

        db.close();
        return n;
    }

    //update
    public void updateNoteById(NotesModel note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] args = {String.valueOf(note.getId())};

        cv.put(COL_TITLE, note.getTitle());
        cv.put(COL_SUBTITLE, note.getSubtitle());
        cv.put(COL_NOTE, note.getNote());
        cv.put(COL_COLOUR, note.getColour());
        cv.put(COL_CREATED_ON, note.getCreated());
        cv.put(COL_UPDATED_ON, note.getUpdated());

        db.update(NOTES_TABLE, cv, COL_ID + " = ?", args);
        db.close();
    }

    //delete
    public void deleteNote(NotesModel note) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {String.valueOf(note.getId())};
        db.delete(NOTES_TABLE, COL_ID + " = ?", args);
        db.close();
    }
}
