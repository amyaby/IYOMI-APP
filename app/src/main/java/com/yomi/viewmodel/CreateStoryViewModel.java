package com.yomi.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PlayerEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateStoryViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    
    private final MutableLiveData<Integer> panelCount = new MutableLiveData<>(4);
    private final MutableLiveData<String> inviteCode = new MutableLiveData<>();
    private final MutableLiveData<List<PlayerEntity>> selectedPlayers = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>(false);
    private final MutableLiveData<String> inviteStatus = new MutableLiveData<>();

    public CreateStoryViewModel(@NonNull Application application) {
        super(application);
        this.repository = new YomiRepository(application);
        this.sessionManager = new SessionManager(application);
        generateNewInviteCode();
    }

    public LiveData<Integer> getPanelCount() { return panelCount; }
    public LiveData<String> getInviteCode() { return inviteCode; }
    public LiveData<List<PlayerEntity>> getSelectedPlayers() { return selectedPlayers; }
    public LiveData<Boolean> getNavigateToHome() { return navigateToHome; }
    public LiveData<String> getInviteStatus() { return inviteStatus; }

    public LiveData<List<PlayerEntity>> getAllPlayers() {
        return repository.getAllPlayers();
    }

    public void incrementPanels() {
        Integer current = panelCount.getValue();
        if (current != null && current < 12) panelCount.setValue(current + 1);
    }

    public void decrementPanels() {
        Integer current = panelCount.getValue();
        if (current != null && current > 2) panelCount.setValue(current - 1);
    }

    public void setTimer(int minutes) {
        // timer logic if needed
    }

    public void addPlayer(PlayerEntity player) {
        List<PlayerEntity> current = selectedPlayers.getValue();
        if (current != null) {
            boolean alreadyIn = current.stream().anyMatch(p -> p.getId() == player.getId());
            if (!alreadyIn) {
                current.add(player);
                selectedPlayers.setValue(new ArrayList<>(current));
            }
        }
    }

    public void addPlayerById(long id) {
        repository.getPlayerById(id, player -> {
            if (player != null) {
                new Handler(Looper.getMainLooper()).post(() -> addPlayer(player));
            }
        });
    }

    public void addPlayerByUsername(String username) {
        if (username == null || username.isEmpty()) return;
        final String searchName = username.startsWith("@") ? username.substring(1) : username;

        repository.getPlayerByUsername(searchName, player -> {
            if (player != null) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    addPlayer(player);
                    inviteStatus.setValue("Joueur ajouté !");
                });
            } else {
                PlayerEntity newPlayer = new PlayerEntity(searchName, searchName + "@yomi.com", "pass", "👤", "#7B5EA7");
                repository.insertPlayer(newPlayer, id -> {
                    newPlayer.setId(id);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        addPlayer(newPlayer);
                        inviteStatus.setValue("Nouveau joueur invité !");
                    });
                });
            }
        });
    }

    public void removePlayer(PlayerEntity player) {
        List<PlayerEntity> current = selectedPlayers.getValue();
        if (current != null) {
            current.removeIf(p -> p.getId() == player.getId());
            selectedPlayers.setValue(new ArrayList<>(current));
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
                    if (p.getId() != creatorId) orderIds.add(p.getId());
                }
            }
            
            String playerOrder = orderIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            StoryEntity story = new StoryEntity(title, creatorId, count, code);
            story.setPlayerOrder(playerOrder);
            
            repository.insertStory(story);
            navigateToHome.postValue(true);
        }
    }
}
