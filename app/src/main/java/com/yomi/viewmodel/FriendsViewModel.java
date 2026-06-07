package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.yomi.database.PlayerEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.util.List;

public class FriendsViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final long currentUserId;

    public FriendsViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
        currentUserId = sessionManager.getPlayerId();
    }

    public LiveData<List<PlayerEntity>> getSuggestions() {
        return repository.getFriendSuggestions(currentUserId);
    }

    public LiveData<List<PlayerEntity>> getFriends() {
        return repository.getFriends(currentUserId);
    }

    public void addFriend(long friendId) {
        repository.addFriend(currentUserId, friendId);
    }

    public void removeFriend(long friendId) {
        repository.removeFriend(currentUserId, friendId);
    }
}
