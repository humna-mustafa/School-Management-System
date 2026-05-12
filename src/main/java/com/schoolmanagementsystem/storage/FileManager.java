package com.schoolmanagementsystem.storage;

import com.schoolmanagementsystem.utils.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Creates the data folder on first run and reads or writes whole text files as line lists.
public class FileManager {
    public void initialize() {
        try {
            Files.createDirectories(Constants.DATA_DIR);
            for (String file : List.of(
                    Constants.SCHOOL_FILE, Constants.USERS_FILE, Constants.STUDENTS_FILE,
                    Constants.TEACHERS_FILE, Constants.SUBJECTS_FILE, Constants.FEES_FILE
            )) {
                Path path = Constants.DATA_DIR.resolve(file);
                if (Files.notExists(path)) {
                    Files.createFile(path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to initialize data folder", e);
        }
    }

    public List<String> readAll(String fileName) {
        try {
            Path path = Constants.DATA_DIR.resolve(fileName);
            if (Files.notExists(path)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + fileName, e);
        }
    }

    public void writeAll(String fileName, List<String> lines) {
        try {
            Files.write(Constants.DATA_DIR.resolve(fileName), lines);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + fileName, e);
        }
    }
}
