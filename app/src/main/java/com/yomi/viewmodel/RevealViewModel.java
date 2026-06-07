package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.yomi.database.PanelEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.List;

public class RevealViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;

    public RevealViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
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

    public LiveData<Integer> getTotalReactions(long storyId) {
        return repository.getReactionCount(storyId);
    }

    public void vote(long storyId, String emoji) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            // Using -1 for story-wide reactions
            repository.insertReaction(new ReactionEntity(storyId, -1, playerId, emoji));
        }
    }
    
    public void voteForPanel(long storyId, int panelIndex, String emoji) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.insertReaction(new ReactionEntity(storyId, panelIndex, playerId, emoji));
        }
    }
}
