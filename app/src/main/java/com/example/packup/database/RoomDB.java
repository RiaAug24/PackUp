package com.example.packup.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.packup.dao.ItemsDao;
import com.example.packup.models.Items;

@Database(entities = Items.class, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;
    private static String DATABASE_NAME = "MyDB";

    public synchronized static RoomDB getInstance(Context context) {
            if (database == null) {
                database = Room.databaseBuilder(context.getApplicationContext(),
                RoomDB.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
            return database;
    }


    public abstract ItemsDao mainDao();

}
