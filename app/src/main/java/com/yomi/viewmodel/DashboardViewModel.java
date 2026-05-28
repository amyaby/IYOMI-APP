package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.database.StoryEntity;
import com.yomi.model.Mappers;
import com.yomi.model.Story;
import com.yomi.model.StoryStatus;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final LiveData<List<Story>> activeStories;
    private final LiveData<List<Story>> completedStories;
    private final MediatorLiveData<Story> turnNotification = new MediatorLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
        
        long playerId = sessionManager.getPlayerId();

        activeStories = Transformations.map(repository.getActiveStoriesForPlayer(playerId), entities -> {
            List<Story> stories = new ArrayList<>();
            if (entities != null) {
                for (StoryEntity entity : entities) {
                    stories.add(Mappers.fromEntity(entity, null));
                }
            }
            return stories;
        });

        completedStories = Transformations.map(repository.getCompletedStories(), entities -> {
            List<Story> stories = new ArrayList<>();
            if (entities != null) {
                for (StoryEntity entity : entities) {
                    stories.add(Mappers.fromEntity(entity, null));
                }
            }
            return stories;
        });

        turnNotification.addSource(activeStories, stories -> {
            if (stories != null) {
                long currentUserId = sessionManager.getPlayerId();
                for (Story story : stories) {
                    if (story.getStatus() == StoryStatus.IN_PROGRESS) {
                        List<Long> order = story.getPlayerOrder();
                        int index = story.getCurrentPanelIndex();
                        if (index < order.size() && order.get(index) == currentUserId) {
                            turnNotification.setValue(story);
                            return;
                        }
                    }
                }
            }
            turnNotification.setValue(null);
        });
    }

    public LiveData<List<Story>> getActiveStories() { return activeStories; }
    public LiveData<List<Story>> getCompletedStories() { return completedStories; }
    public LiveData<Story> getTurnNotification() { return turnNotification; }
    public LiveData<List<com.yomi.database.PlayerEntity>> getAllPlayers() { return repository.getAllPlayers(); }
}
