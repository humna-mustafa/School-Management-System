package com.schoolmanagementsystem.utils;

import java.nio.file.Path;

// Names the data folder and the text file names stored inside it.
public final class Constants {
    public static final Path DATA_DIR = Path.of("data");
    public static final String SCHOOL_FILE = "school.txt";
    public static final String USERS_FILE = "users.txt";
    public static final String STUDENTS_FILE = "students.txt";
    public static final String TEACHERS_FILE = "teachers.txt";
    public static final String SUBJECTS_FILE = "subjects.txt";
    public static final String FEES_FILE = "fees.txt";

    private Constants() {
    }
}
