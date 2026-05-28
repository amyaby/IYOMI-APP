package com.yomi.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.yomi.database.PlayerEntity;
import java.util.List;

@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PlayerEntity player);

    @Query("SELECT * FROM players ORDER BY createdAt DESC")
    LiveData<List<PlayerEntity>> getAllPlayers();

    @Query("SELECT * FROM players WHERE id = :id")
    PlayerEntity getById(long id);

    @Query("SELECT * FROM players WHERE email = :email AND password = :password LIMIT 1")
    PlayerEntity login(String email, String password);

    @Query("SELECT COUNT(*) FROM players")
    int count();
}
