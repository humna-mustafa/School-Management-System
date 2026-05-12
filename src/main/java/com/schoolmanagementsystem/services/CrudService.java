package com.schoolmanagementsystem.services;

import com.schoolmanagementsystem.models.EntityRecord;
import com.schoolmanagementsystem.storage.DataStore;

import java.util.List;

// Loads and saves one kind of row file for the app, such as all students or all fees.
// Each screen gets its own service object that knows which file name to use.
public class CrudService {
    private final DataStore dataStore;
    private final String fileName;

    public CrudService(DataStore dataStore, String fileName) {
        this.dataStore = dataStore;
        this.fileName = fileName;
    }

    public List<EntityRecord> getAll() {
        return dataStore.loadRecords(fileName);
    }

    public void saveAll(List<EntityRecord> records) {
        dataStore.saveRecords(fileName, records);
    }
}
