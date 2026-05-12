package com.schoolmanagementsystem.models;

// One generic row used for students, teachers, subjects, and fees in the text files.
// The meaning of the text fields depends on which screen owns the file.
public class EntityRecord {
    private final String id;
    private final String name;
    private final String detail1;
    private final String detail2;
    private final String status;

    public EntityRecord(String id, String name, String detail1, String detail2, String status) {
        this.id = id;
        this.name = name;
        this.detail1 = detail1;
        this.detail2 = detail2;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail1() {
        return detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public String getStatus() {
        return status;
    }

    public String toLine() {
        return String.join("|", id, name, detail1, detail2, status);
    }

    public static EntityRecord fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 5) {
            return null;
        }
        return new EntityRecord(p[0], p[1], p[2], p[3], p[4]);
    }
}
