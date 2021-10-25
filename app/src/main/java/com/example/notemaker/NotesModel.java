package com.example.notemaker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotesModel {
    private int id;
    private String title;
    private String subtitle;
    private String note;
    private String colour;
    private LocalDateTime created;
    private LocalDateTime updated;

    public NotesModel(String title, String subtitle, String note, String colour, LocalDateTime created, LocalDateTime updated) {
        this.title = title;
        this.subtitle = subtitle;
        this.note = note;
        this.colour = colour;
        this.created = created;
        this.updated = updated;
    }

    public NotesModel(int id, String title, String subtitle, String note, String colour, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.note = note;
        this.colour = colour;
        this.created = created;
        this.updated = updated;
    }

    @Override
    public String toString() {
//        return "NotesModel{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", subtitle='" + subtitle + '\'' +
//                ", note='" + note + '\'' +
//                ", colour='" + colour + '\'' +
//                ", created=" + created +
//                ", updated=" + updated +
//                '}';

        // Updating the toString
        return title + " - " + subtitle + ": " + note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCreated() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(created);
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getUpdated() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(updated);
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
