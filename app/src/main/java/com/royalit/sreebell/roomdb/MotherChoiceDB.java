package com.royalit.sreebell.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = CartItems.class, exportSchema = false, version = 3)
public abstract class MotherChoiceDB extends RoomDatabase {

    private static final String DATA_BASE_NAME = "mother_choice";
    private static MotherChoiceDB instance;

    public static synchronized MotherChoiceDB getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MotherChoiceDB.class, DATA_BASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract CartDAO cartDAO();
}
