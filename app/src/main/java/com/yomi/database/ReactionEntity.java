package com.yomi.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "reactions",
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
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long storyId;
    private int panelIndex; // Added for panel-level reactions
    private long playerId;
    @NonNull
    private String emoji;
    private long createdAt;

    public ReactionEntity(long storyId, int panelIndex, long playerId, @NonNull String emoji) {
        this.storyId = storyId;
        this.panelIndex = panelIndex;
        this.playerId = playerId;
        this.emoji = emoji;
        this.createdAt = System.currentTimeMillis();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getStoryId() { return storyId; }
    public void setStoryId(long storyId) { this.storyId = storyId; }
    public int getPanelIndex() { return panelIndex; }
    public void setPanelIndex(int panelIndex) { this.panelIndex = panelIndex; }
    public long getPlayerId() { return playerId; }
    public void setPlayerId(long playerId) { this.playerId = playerId; }
    @NonNull
    public String getEmoji() { return emoji; }
    public void setEmoji(@NonNull String emoji) { this.emoji = emoji; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
