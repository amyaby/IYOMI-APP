package com.yomi.domain.usecases;

import androidx.lifecycle.LiveData;
import com.yomi.database.StoryEntity;
import com.yomi.database.StoryWithReactions;
import com.yomi.repository.StoryRepository;
import java.util.List;

public class GetStoriesUseCase {
    private final StoryRepository repository;

    public GetStoriesUseCase(StoryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<StoryEntity>> getActiveStories() {
        return repository.getActiveStories();
    }

    public LiveData<List<StoryEntity>> getActiveStoriesForPlayer(long playerId) {
        return repository.getActiveStoriesForPlayer(playerId);
    }

    public LiveData<List<StoryEntity>> getCompletedStories() {
        return repository.getCompletedStories();
    }

    public LiveData<List<StoryWithReactions>> getCompletedStoriesWithReactions() {
        return repository.getCompletedStoriesWithReactions();
    }
}
