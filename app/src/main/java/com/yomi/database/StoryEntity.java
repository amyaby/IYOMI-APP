package com.yomi.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "stories",
        foreignKeys = @ForeignKey(entity = PlayerEntity.class,
                parentColumns = "id",
                childColumns = "creatorId",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("creatorId"))
public class StoryEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private long creatorId;
    private int totalPanels;
    private int currentPanelIndex;
    private String status;
    private String playerOrder;
    private long createdAt;
    private Long completedAt;
    private String inviteCode;

    public StoryEntity(String title, long creatorId, int totalPanels, String inviteCode) {
        this.title = title;
        this.creatorId = creatorId;
        this.totalPanels = totalPanels;
        this.currentPanelIndex = 0;
        this.status = "IN_PROGRESS";
        this.playerOrder = "";
        this.createdAt = System.currentTimeMillis();
        this.inviteCode = inviteCode;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public long getCreatorId() { return creatorId; }
    public int getTotalPanels() { return totalPanels; }
    public int getCurrentPanelIndex() { return currentPanelIndex; }
    public void setCurrentPanelIndex(int currentPanelIndex) { this.currentPanelIndex = currentPanelIndex; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPlayerOrder() { return playerOrder; }
    public void setPlayerOrder(String playerOrder) { this.playerOrder = playerOrder; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public Long getCompletedAt() { return completedAt; }
    public void setCompletedAt(Long completedAt) { this.completedAt = completedAt; }
    public String getInviteCode() { return inviteCode; }
}
