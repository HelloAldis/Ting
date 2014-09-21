
package com.kindazrael.tingweather.dao;

import android.database.sqlite.SQLiteDatabase;

import com.kindazrael.tingweather.db.DatabaseHelper;

public class DaoBase {

    protected static void closeDatabase(SQLiteDatabase database) {
        if (database != null & database.isOpen()) {
            try {
                database.close();
            } catch (Exception e) {
            }
        }
    }

    protected static SQLiteDatabase getDatabase() {
        return DatabaseHelper.getInstance().getWritableDatabase();
    }

    protected static void executeSQLiteNoQueryOperation(
            SQLiteNoQueryOperation operation) {
        SQLiteDatabase database = null;

        try {
            database = getDatabase();
            operation.execute(database);
        } finally {
            closeDatabase(database);
        }
    }

    protected interface SQLiteNoQueryOperation {

        public void execute(SQLiteDatabase database);
    }

}
