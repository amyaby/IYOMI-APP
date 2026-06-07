package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.R;
import com.yomi.database.PlayerEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
import com.yomi.database.StoryWithReactions;
import com.yomi.model.Mappers;
import com.yomi.model.Story;
import com.yomi.model.StoryStatus;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final LiveData<List<Story>> allActiveStories;
    private final LiveData<List<Story>> allCompletedStories;
    private final LiveData<List<PlayerEntity>> allCollaborators;
    private final MediatorLiveData<Story> notificationStory = new MediatorLiveData<>();
    private final MediatorLiveData<String> notificationMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Integer> notificationCount = new MediatorLiveData<>();
    private final LiveData<List<ReactionEntity>> userReactions;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.repository = new YomiRepository(application);
        this.sessionManager = new SessionManager(application);
        
        long currentUserId = sessionManager.getPlayerId();

        // 1. Fetch ALL users for the community list (Collaborateurs Suggérés)
        allCollaborators = Transformations.map(repository.getAllPlayers(), players -> {
            if (players == null) return new ArrayList<>();
            return players.stream()
                    .filter(p -> p.getId() != currentUserId)
                    .collect(Collectors.toList());
        });

        // 2. Universal Visibility: Fetch ALL active and completed stories
        allActiveStories = Transformations.map(repository.getActiveStories(), entities -> {
            List<Story> stories = new ArrayList<>();
            if (entities != null) {
                for (StoryEntity entity : entities) {
                    stories.add(Mappers.fromEntity(entity, null));
                }
            }
            return stories;
        });

        allCompletedStories = Transformations.map(repository.getCompletedStoriesWithReactions(), entities -> {
            List<Story> stories = new ArrayList<>();
            if (entities != null) {
                for (StoryWithReactions swr : entities) {
                    stories.add(Mappers.fromWithReactions(swr));
                }
            }
            return stories;
        });

        userReactions = repository.getReactionsForPlayerStories(currentUserId);

        // 3. Consolidated Notification Logic (Priority: Turn > Reactions > Invitation)
        notificationMessage.addSource(allActiveStories, stories -> calculateNotifications(stories, userReactions.getValue(), currentUserId));
        notificationMessage.addSource(userReactions, reactions -> calculateNotifications(allActiveStories.getValue(), reactions, currentUserId));
    }

    private void calculateNotifications(List<Story> stories, List<ReactionEntity> reactions, long currentUserId) {
        String message = null;
        Story targetStory = null;

        if (stories != null) {
            // Priority 1: Your Turn
            for (Story story : stories) {
                if (story.getStatus() == StoryStatus.IN_PROGRESS) {
                    List<Long> order = story.getPlayerOrder();
                    int index = story.getCurrentPanelIndex();
                    if (index < order.size() && order.get(index) == currentUserId) {
                        message = getApplication().getString(R.string.notification_turn_format, story.getTitle());
                        targetStory = story;
                        break;
                    }
                }
            }
        }

        // Priority 2: New Reactions on your creations
        if (message == null && reactions != null && !reactions.isEmpty()) {
            ReactionEntity latest = reactions.get(0);
            message = "Quelqu'un a réagi " + latest.getEmoji() + " à ta BD !";
        }

        // Priority 3: New Invitation
        if (message == null && stories != null) {
            for (Story story : stories) {
                if (story.getCurrentPanelIndex() == 0 && story.getCreatorId() != currentUserId 
                    && story.getPlayerOrder().contains(currentUserId)) {
                    message = "Tu as été ajouté à une nouvelle BD : \"" + story.getTitle() + "\" !";
                    targetStory = story;
                    break;
                }
            }
        }

        notificationMessage.setValue(message);
        notificationStory.setValue(targetStory);
        notificationCount.setValue(message != null ? 1 : 0);
    }

    public LiveData<List<Story>> getActiveStories() { return allActiveStories; }
    public LiveData<List<Story>> getCompletedStories() { return allCompletedStories; }
    public LiveData<String> getNotificationMessage() { return notificationMessage; }
    public LiveData<Story> getNotificationStory() { return notificationStory; }
    public LiveData<Integer> getNotificationCount() { return notificationCount; }
    public LiveData<List<PlayerEntity>> getCollaborators() { return allCollaborators; }

    public void vote(long storyId, String emoji) {
        long playerId = sessionManager.getPlayerId();
        if (playerId != -1) {
            repository.insertReaction(new ReactionEntity(storyId, -1, playerId, emoji));
        }
    }
}
