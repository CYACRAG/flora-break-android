package com.florabreak.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Lokale Room-Datenbank für Flora Break.
 *
 * Speichert Pausen, Bewertungen, Stresswerte und Foto-Streckenbeweise.
 */
@Database(
        entities = {BreakEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class FloraBreakDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "flora_break_database";

    private static volatile FloraBreakDatabase instance;

    public abstract BreakDao breakDao();

    public static FloraBreakDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (FloraBreakDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    FloraBreakDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return instance;
    }
}
