package com.team03.dtuevent.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.team03.dtuevent.App;
import com.team03.dtuevent.objects.Code;


@Database(entities = {com.team03.dtuevent.database.CodeMemento.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HistoryDatabase extends RoomDatabase {


    private static HistoryDatabase INSTANCE;

    public abstract com.team03.dtuevent.database.HistoryDao historyDao();

    public static synchronized HistoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "code")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void insertCode(Context context, Code code) {
        // Open Database and insert item
        com.team03.dtuevent.database.HistoryDao dao = HistoryDatabase.getInstance(context).historyDao();
        com.team03.dtuevent.database.CodeMemento codeHistoryElement = code.toMemento();
        App.globalExService.submit(() -> dao.add(codeHistoryElement));
    }
}
