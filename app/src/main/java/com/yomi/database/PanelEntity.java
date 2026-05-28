package com.yomi.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "panels",
        foreignKeys = {
                @ForeignKey(entity = StoryEntity.class,
                        parentColumns = "id",
                        childColumns = "storyId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = PlayerEntity.class,
                        parentColumns = "id",
                        childColumns = "authorId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("storyId"), @Index("authorId")})
public class PanelEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long storyId;
    private long authorId;
    private String authorName;
    private int panelIndex;
    private String imagePath;
    private String dialogText;
    private long submittedAt;
    private boolean isEmpty;

    public PanelEntity(long storyId, long authorId, String authorName, int panelIndex, String imagePath, String dialogText, boolean isEmpty) {
        this.storyId = storyId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.panelIndex = panelIndex;
        this.imagePath = imagePath;
        this.dialogText = dialogText;
        this.submittedAt = System.currentTimeMillis();
        this.isEmpty = isEmpty;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getStoryId() { return storyId; }
    public long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public int getPanelIndex() { return panelIndex; }
    public String getImagePath() { return imagePath; }
    public String getDialogText() { return dialogText; }
    public long getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(long submittedAt) { this.submittedAt = submittedAt; }
    public boolean isEmpty() { return isEmpty; }
}
