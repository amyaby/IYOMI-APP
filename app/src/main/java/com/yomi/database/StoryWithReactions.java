package com.yomi.database;

import androidx.room.Embedded;

public class StoryWithReactions {
    @Embedded
    public StoryEntity story;
    public int reactionCount;
}
