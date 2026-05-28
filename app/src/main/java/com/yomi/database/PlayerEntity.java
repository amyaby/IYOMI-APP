package com.yomi.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "players")
public class PlayerEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String password;
    private String avatarEmoji;
    private String avatarColor;
    private long createdAt;

    public PlayerEntity(String name, String email, String password, String avatarEmoji, String avatarColor) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatarEmoji = avatarEmoji;
        this.avatarColor = avatarColor;
        this.createdAt = System.currentTimeMillis();
    }

    // Add back 3-arg constructor for existing code/backward compatibility
    @Ignore
    public PlayerEntity(String name, String avatarEmoji, String avatarColor) {
        this(name, "", "", avatarEmoji, avatarColor);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatarEmoji() { return avatarEmoji; }
    public void setAvatarEmoji(String avatarEmoji) { this.avatarEmoji = avatarEmoji; }
    public String getAvatarColor() { return avatarColor; }
    public void setAvatarColor(String avatarColor) { this.avatarColor = avatarColor; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
