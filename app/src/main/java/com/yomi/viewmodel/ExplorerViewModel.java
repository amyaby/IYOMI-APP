package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryWithReactions;
import com.yomi.model.Mappers;
import com.yomi.model.Story;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExplorerViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final LiveData<List<Story>> joinableStories;
    private final LiveData<List<Story>> allStories;
    private final LiveData<List<Story>> leaderboardStories;
    private final LiveData<Story> featuredStory;
    private final MutableLiveData<Boolean> showPopularity = new MutableLiveData<>(true);

    public ExplorerViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
        
        allStories = Transformations.map(repository.getAllStoriesWithReactions(), entities -> {
            if (entities == null) return new ArrayList<>();
            return entities.stream().map(Mappers::fromWithReactions).collect(Collectors.toList());
        });
        
        leaderboardStories = allStories;
        
        joinableStories = Transformations.map(repository.getActiveStoriesWithReactions(), entities -> {
            if (entities == null) return new ArrayList<>();
            return entities.stream().map(Mappers::fromWithReactions).collect(Collectors.toList());
        });

        featuredStory = Transformations.map(repository.getCompletedStoriesWithReactions(), list -> 
            (list != null && !list.isEmpty()) ? Mappers.fromWithReactions(list.get(0)) : null
        );
    }

    public LiveData<Boolean> getShowPopularity() { return showPopularity; }
    public void setShowPopularity(boolean popularity) { showPopularity.setValue(popularity); }
    public LiveData<List<Story>> getPopularStories() { return allStories; }
    public LiveData<List<Story>> getLeaderboardStories() { return leaderboardStories; }
    public LiveData<List<Story>> getJoinableStories() { return joinableStories; }
    public LiveData<Story> getFeaturedStory() { return featuredStory; }

    public void vote(long storyId, String emoji) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.insertReaction(new ReactionEntity(storyId, -1, playerId, emoji));
        }
    }
    
    public void joinStory(long storyId) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.joinStory(storyId, playerId, success -> { });
        }
    }
}
