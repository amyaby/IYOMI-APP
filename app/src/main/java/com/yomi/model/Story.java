package com.yomi.model;

import java.util.List;

public class Story {
    private final long id;
    private final String title;
    private final long creatorId;
    private final int totalPanels;
    private final int currentPanelIndex;
    private final StoryStatus status;
    private final List<Long> playerOrder;
    private final List<Panel> panels;
    private final long createdAt;
    private final String inviteCode;
    private final int reactionCount;

    public Story(long id, String title, long creatorId, int totalPanels, int currentPanelIndex, 
                 StoryStatus status, List<Long> playerOrder, List<Panel> panels, 
                 long createdAt, String inviteCode, int reactionCount) {
        this.id = id;
        this.title = title;
        this.creatorId = creatorId;
        this.totalPanels = totalPanels;
        this.currentPanelIndex = currentPanelIndex;
        this.status = status;
        this.playerOrder = playerOrder;
        this.panels = panels;
        this.createdAt = createdAt;
        this.inviteCode = inviteCode;
        this.reactionCount = reactionCount;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public long getCreatorId() { return creatorId; }
    public int getTotalPanels() { return totalPanels; }
    public int getCurrentPanelIndex() { return currentPanelIndex; }
    public StoryStatus getStatus() { return status; }
    public List<Long> getPlayerOrder() { return playerOrder; }
    public List<Panel> getPanels() { return panels; }
    public long getCreatedAt() { return createdAt; }
    public String getInviteCode() { return inviteCode; }
    public int getReactionCount() { return reactionCount; }
}
