package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
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
    private final MutableLiveData<Boolean> showPopularity = new MutableLiveData<>(true);
    private final LiveData<List<Story>> joinableStories;
    private final LiveData<List<StoryEntity>> popularStories;

    public ExplorerViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
        
        popularStories = repository.getActiveStories();
        
        // Mock joinable stories for now, but as domain models
        MutableLiveData<List<StoryEntity>> mockEntities = new MutableLiveData<>();
        List<StoryEntity> mockList = new ArrayList<>();
        mockList.add(new StoryEntity("L'océan qui rêvait", 1L, 4, "JOIN1"));
        mockList.add(new StoryEntity("Métamorphose Express", 2L, 5, "JOIN2"));
        mockEntities.setValue(mockList);

        joinableStories = Transformations.map(mockEntities, entities -> 
            entities.stream().map(e -> Mappers.fromEntity(e, null)).collect(Collectors.toList())
        );
    }

    public LiveData<Boolean> getShowPopularity() {
        return showPopularity;
    }

    public void setShowPopularity(boolean popularity) {
        showPopularity.setValue(popularity);
    }

    public LiveData<List<StoryEntity>> getPopularStories() {
        return popularStories;
    }

    public LiveData<List<Story>> getJoinableStories() {
        return joinableStories;
    }

    public void vote(long storyId, String emoji) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.insertReaction(new ReactionEntity(storyId, playerId, emoji));
        }
    }
    
    public void joinStory(long storyId) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.joinStory(storyId, playerId, success -> {
                // Handle success/failure if needed
            });
        }
    }
}
