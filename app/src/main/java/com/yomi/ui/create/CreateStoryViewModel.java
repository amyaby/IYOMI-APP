package com.yomi.ui.create;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PlayerEntity;
import com.yomi.database.StoryEntity;
import com.yomi.domain.usecases.CreateStoryUseCase;
import com.yomi.repository.SessionManager;
import com.yomi.repository.StoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateStoryViewModel extends AndroidViewModel {
    private final CreateStoryUseCase createStoryUseCase;
    private final SessionManager sessionManager;
    private final StoryRepository repository;
    private final MutableLiveData<Integer> panelCount = new MutableLiveData<>(4);
    private final MutableLiveData<String> inviteCode = new MutableLiveData<>();
    private final MutableLiveData<List<PlayerEntity>> selectedPlayers = new MutableLiveData<>(new ArrayList<>());

    public CreateStoryViewModel(@NonNull Application application) {
        super(application);
        this.repository = new StoryRepository(application);
        this.createStoryUseCase = new CreateStoryUseCase(repository);
        this.sessionManager = new SessionManager(application);
        generateNewInviteCode();
    }

    public LiveData<Integer> getPanelCount() {
        return panelCount;
    }

    public LiveData<String> getInviteCode() {
        return inviteCode;
    }

    public LiveData<List<PlayerEntity>> getSelectedPlayers() {
        return selectedPlayers;
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() {
        return repository.getAllPlayers();
    }

    public void incrementPanels() {
        Integer current = panelCount.getValue();
        if (current != null) {
            panelCount.setValue(current + 1);
        }
    }

    public void decrementPanels() {
        Integer current = panelCount.getValue();
        if (current != null && current > 2) {
            panelCount.setValue(current - 1);
        }
    }

    public void addPlayer(PlayerEntity player) {
        List<PlayerEntity> current = selectedPlayers.getValue();
        if (current != null && !current.contains(player)) {
            current.add(player);
            selectedPlayers.setValue(current);
        }
    }

    public void removePlayer(PlayerEntity player) {
        List<PlayerEntity> current = selectedPlayers.getValue();
        if (current != null) {
            current.remove(player);
            selectedPlayers.setValue(current);
        }
    }

    public void generateNewInviteCode() {
        inviteCode.setValue(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
    }

    public void createStory(String title) {
        Integer count = panelCount.getValue();
        String code = inviteCode.getValue();
        List<PlayerEntity> players = selectedPlayers.getValue();
        
        if (title != null && !title.isEmpty() && count != null && code != null) {
            long creatorId = sessionManager.getPlayerId();
            
            List<Long> orderIds = new ArrayList<>();
            orderIds.add(creatorId);
            if (players != null) {
                for (PlayerEntity p : players) {
                    if (p.getId() != creatorId) {
                        orderIds.add(p.getId());
                    }
                }
            }
            
            String playerOrder = orderIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            StoryEntity story = new StoryEntity(title, creatorId, count, code);
            story.setPlayerOrder(playerOrder);
            createStoryUseCase.execute(story);
        }
    }
}
