package com.yomi.model;

public class Player {
    private final long id;
    private final String name;
    private final String avatarEmoji;
    private final String avatarColor;

    public Player(long id, String name, String avatarEmoji, String avatarColor) {
        this.id = id;
        this.name = name;
        this.avatarEmoji = avatarEmoji;
        this.avatarColor = avatarColor;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getAvatarEmoji() { return avatarEmoji; }
    public String getAvatarColor() { return avatarColor; }
}
