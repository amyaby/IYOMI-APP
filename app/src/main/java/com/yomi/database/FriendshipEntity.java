package com.yomi.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "friendships",
        primaryKeys = {"userId", "friendId"},
        foreignKeys = {
                @ForeignKey(entity = PlayerEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = PlayerEntity.class,
                        parentColumns = "id",
                        childColumns = "friendId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("friendId")})
public class FriendshipEntity {
    private long userId;
    private long friendId;
    private long createdAt;

    public FriendshipEntity(long userId, long friendId) {
        this.userId = userId;
        this.friendId = friendId;
        this.createdAt = System.currentTimeMillis();
    }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public long getFriendId() { return friendId; }
    public void setFriendId(long friendId) { this.friendId = friendId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
