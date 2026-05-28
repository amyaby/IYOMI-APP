package com.yomi.model;

public class Panel {
    private final long id;
    private final long storyId;
    private final long authorId;
    private final String authorName;
    private final int panelIndex;
    private final String imagePath;
    private final String dialogText;
    private final boolean isEmpty;

    public Panel(long id, long storyId, long authorId, String authorName, int panelIndex, String imagePath, String dialogText, boolean isEmpty) {
        this.id = id;
        this.storyId = storyId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.panelIndex = panelIndex;
        this.imagePath = imagePath;
        this.dialogText = dialogText;
        this.isEmpty = isEmpty;
    }

    public long getId() { return id; }
    public long getStoryId() { return storyId; }
    public long getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public int getPanelIndex() { return panelIndex; }
    public String getImagePath() { return imagePath; }
    public String getDialogText() { return dialogText; }
    public boolean isEmpty() { return isEmpty; }
}
