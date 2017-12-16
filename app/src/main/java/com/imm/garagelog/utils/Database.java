package com.imm.garagelog.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ionut on 16/12/2017.
 */

public class Database {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }
}
