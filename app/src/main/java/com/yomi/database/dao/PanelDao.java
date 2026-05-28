package com.yomi.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yomi.database.PanelEntity;
import java.util.List;

@Dao
public interface PanelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PanelEntity panel);

    @Query("SELECT * FROM panels WHERE storyId = :storyId ORDER BY panelIndex ASC")
    LiveData<List<PanelEntity>> getPanelsForStory(long storyId);

    @Query("SELECT * FROM panels WHERE storyId = :storyId ORDER BY panelIndex ASC")
    List<PanelEntity> getPanelsForStorySync(long storyId);
}
