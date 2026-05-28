package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PlayerEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;

public class PlayersViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<Boolean> playerCreated = new MutableLiveData<>(false);

    public PlayersViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getPlayerCreated() {
        return playerCreated;
    }

    public void createPlayer(String name, String emoji, String color) {
        PlayerEntity player = new PlayerEntity(name, emoji, color);
        
        repository.insertPlayer(player, id -> {
            // Save the real ID from the database into the session
            sessionManager.createSession(id, name);
            playerCreated.postValue(true);
        });
    }
}
