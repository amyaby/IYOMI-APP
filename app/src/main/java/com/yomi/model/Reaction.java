package com.yomi.model;

public class Reaction {
    private final long storyId;
    private final long playerId;
    private final String emoji;

    public Reaction(long storyId, long playerId, String emoji) {
        this.storyId = storyId;
        this.playerId = playerId;
        this.emoji = emoji;
    }

    public long getStoryId() { return storyId; }
    public long getPlayerId() { return playerId; }
    public String getEmoji() { return emoji; }
}
