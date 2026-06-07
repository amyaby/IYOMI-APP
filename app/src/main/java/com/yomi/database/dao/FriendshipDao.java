package com.yomi.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yomi.database.PlayerEntity;
import java.util.List;

@Dao
public interface FriendshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(com.yomi.database.FriendshipEntity friendship);

    @Delete
    void delete(com.yomi.database.FriendshipEntity friendship);

    @Query("SELECT p.* FROM players p INNER JOIN friendships f ON p.id = f.friendId WHERE f.userId = :userId")
    LiveData<List<PlayerEntity>> getFriends(long userId);

    @Query("SELECT * FROM players WHERE id NOT IN (SELECT friendId FROM friendships WHERE userId = :userId) AND id != :userId")
    LiveData<List<PlayerEntity>> getSuggestions(long userId);
}
