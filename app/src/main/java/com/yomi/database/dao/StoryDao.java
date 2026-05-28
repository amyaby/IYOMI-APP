package com.yomi.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.yomi.database.StoryEntity;
import java.util.List;

@Dao
public interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(StoryEntity story);

    @Update
    int update(StoryEntity story);

    @Query("SELECT * FROM stories ORDER BY createdAt DESC")
    LiveData<List<StoryEntity>> getAllStories();

    @Query("SELECT * FROM stories WHERE id = :id")
    LiveData<StoryEntity> getStoryById(long id);

    @Query("SELECT * FROM stories WHERE id = :id")
    StoryEntity getStoryByIdSync(long id);

    @Query("SELECT * FROM stories WHERE status = 'IN_PROGRESS' ORDER BY createdAt DESC")
    LiveData<List<StoryEntity>> getActiveStories();

    @Query("SELECT * FROM stories WHERE status = 'IN_PROGRESS' AND (creatorId = :playerId OR playerOrder LIKE '%' || :playerId || '%') ORDER BY createdAt DESC")
    LiveData<List<StoryEntity>> getActiveStoriesForPlayer(long playerId);

    @Query("SELECT * FROM stories WHERE status = 'COMPLETED' ORDER BY completedAt DESC")
    LiveData<List<StoryEntity>> getCompletedStories();

    @Query("UPDATE stories SET currentPanelIndex = :index WHERE id = :storyId")
    int updatePanelIndex(long storyId, int index);

    @Query("UPDATE stories SET status = 'COMPLETED', completedAt = :time WHERE id = :storyId")
    int markCompleted(long storyId, long time);

    @Query("SELECT COUNT(*) FROM stories WHERE creatorId = :playerId")
    LiveData<Integer> getStoryCountByPlayer(long playerId);
}
