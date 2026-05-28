package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.yomi.database.PanelEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.YomiRepository;
import java.util.List;

public class RevealViewModel extends AndroidViewModel {
    private final YomiRepository repository;

    public RevealViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
    }

    public LiveData<StoryEntity> getStory(long storyId) {
        return repository.getStoryById(storyId);
    }

    public LiveData<List<PanelEntity>> getPanels(long storyId) {
        return repository.getPanelsForStory(storyId);
    }

    public LiveData<List<ReactionEntity>> getReactions(long storyId) {
        return repository.getReactionsForStory(storyId);
    }

    public void addReaction(long storyId, long playerId, String emoji) {
        repository.insertReaction(new ReactionEntity(storyId, playerId, emoji));
    }
}
