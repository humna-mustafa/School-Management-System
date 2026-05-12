package com.schoolmanagementsystem.storage;

import com.schoolmanagementsystem.models.EntityRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Reads lists of generic rows from disk and writes them back after edits.
// The GUI never opens files itself; it always goes through this class.
public class DataStore {
    private final FileManager fileManager;

    public DataStore(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public List<EntityRecord> loadRecords(String fileName) {
        List<EntityRecord> data = new ArrayList<>();
        for (String line : fileManager.readAll(fileName)) {
            if (!line.isBlank()) {
                EntityRecord record = EntityRecord.fromLine(line);
                if (record != null) {
                    data.add(record);
                }
            }
        }
        return data;
    }

    public void saveRecords(String fileName, List<EntityRecord> records) {
        List<String> lines = records.stream().map(EntityRecord::toLine).collect(Collectors.toList());
        fileManager.writeAll(fileName, lines);
    }
}
