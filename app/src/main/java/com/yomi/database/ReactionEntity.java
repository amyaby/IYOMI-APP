package com.yomi.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reactions",
        primaryKeys = {"storyId", "playerId"},
        foreignKeys = {
                @ForeignKey(entity = StoryEntity.class,
                        parentColumns = "id",
                        childColumns = "storyId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = PlayerEntity.class,
                        parentColumns = "id",
                        childColumns = "playerId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("storyId"), @Index("playerId")})
public class ReactionEntity {
    private long storyId;
    private long playerId;
    private String emoji;
    private long createdAt;

    public ReactionEntity(long storyId, long playerId, String emoji) {
        this.storyId = storyId;
        this.playerId = playerId;
        this.emoji = emoji;
        this.createdAt = System.currentTimeMillis();
    }

    public long getStoryId() { return storyId; }
    public long getPlayerId() { return playerId; }
    public String getEmoji() { return emoji; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
