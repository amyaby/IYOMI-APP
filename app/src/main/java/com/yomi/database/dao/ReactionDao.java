package com.yomi.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yomi.database.ReactionEntity;
import java.util.List;

@Dao
public interface ReactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ReactionEntity reaction);

    @Query("SELECT * FROM reactions WHERE storyId = :storyId")
    LiveData<List<ReactionEntity>> getReactionsForStory(long storyId);
}
