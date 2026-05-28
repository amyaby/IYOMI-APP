package com.yomi.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.yomi.database.dao.PanelDao;
import com.yomi.database.dao.PlayerDao;
import com.yomi.database.dao.ReactionDao;
import com.yomi.database.dao.StoryDao;

@Database(entities = {PlayerEntity.class, StoryEntity.class, PanelEntity.class, ReactionEntity.class}, version = 2, exportSchema = false)
public abstract class YomiDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
    public abstract StoryDao storyDao();
    public abstract PanelDao panelDao();
    public abstract ReactionDao reactionDao();

    private static volatile YomiDatabase INSTANCE;

    public static YomiDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (YomiDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    YomiDatabase.class, "yomi_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
