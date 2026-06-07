package com.yomi.ui.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.database.StoryEntity;
import com.yomi.database.StoryWithReactions;
import com.yomi.domain.usecases.GetStoriesUseCase;
import com.yomi.model.Mappers;
import com.yomi.model.Story;
import com.yomi.model.StoryStatus;
import com.yomi.repository.SessionManager;
import com.yomi.repository.StoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel extends AndroidViewModel {
    private final GetStoriesUseCase getStoriesUseCase;
    private final SessionManager sessionManager;
    private final LiveData<List<Story>> activeStories;
    private final LiveData<List<Story>> completedStories;
    private final LiveData<List<StoryWithReactions>> completedStoriesWithReactions;
    private final MediatorLiveData<Story> turnNotification = new MediatorLiveData<>();
    private final StoryRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.repository = new StoryRepository(application);
        this.getStoriesUseCase = new GetStoriesUseCase(repository);
        this.sessionManager = new SessionManager(application);
        
        activeStories = Transformations.map(getStoriesUseCase.getActiveStories(), entities -> {
            List<Story> stories = new ArrayList<>();
            if (entities != null) {
                for (StoryEntity entity : entities) {
                    stories.add(Mappers.fromEntity(entity, null));
                }
            }
            return stories;
        });

        completedStoriesWithReactions = getStoriesUseCase.getCompletedStoriesWithReactions();
        completedStories = Transformations.map(completedStoriesWithReactions, entities -> {
            if (entities == null) return new ArrayList<>();
            return entities.stream().map(Mappers::fromWithReactions).collect(Collectors.toList());
        });

        turnNotification.addSource(activeStories, stories -> {
            if (stories != null && !stories.isEmpty()) {
                for (Story story : stories) {
                    if (story.getStatus() == StoryStatus.IN_PROGRESS) {
                        turnNotification.setValue(story);
                        return;
                    }
                }
            }
            turnNotification.setValue(null);
        });
    }

    public LiveData<List<Story>> getActiveStories() { return activeStories; }
    public LiveData<List<Story>> getCompletedStories() { return completedStories; }
    public LiveData<List<StoryWithReactions>> getCompletedStoriesWithReactions() { return completedStoriesWithReactions; }
    public LiveData<Story> getTurnNotification() { return turnNotification; }
}
