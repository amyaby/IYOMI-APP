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

    @Query("SELECT COUNT(*) FROM reactions WHERE storyId = :storyId")
    LiveData<Integer> getReactionCount(long storyId);

    @Query("SELECT reactions.* FROM reactions INNER JOIN stories ON reactions.storyId = stories.id WHERE stories.creatorId = :playerId ORDER BY reactions.createdAt DESC")
    LiveData<List<ReactionEntity>> getReactionsForPlayerStories(long playerId);
}
