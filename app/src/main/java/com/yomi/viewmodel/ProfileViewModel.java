package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;

public class ProfileViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<String> username = new MutableLiveData<>();
    private final MutableLiveData<String> joinDate = new MutableLiveData<>("mars 2025");
    private final MutableLiveData<String> panelsDrawn = new MutableLiveData<>("0");
    private final MutableLiveData<String> reactionsReceived = new MutableLiveData<>("0");
    private final MutableLiveData<String> streak = new MutableLiveData<>("0");
    private final MutableLiveData<String> ranking = new MutableLiveData<>("#0");

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
        username.setValue(sessionManager.getPlayerName());
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getJoinDate() {
        return joinDate;
    }

    public LiveData<Integer> getStoryCount() {
        return repository.getStoryCountByPlayer(sessionManager.getPlayerId());
    }

    public LiveData<String> getBdsCreated() {
        return Transformations.map(getStoryCount(), String::valueOf);
    }

    public LiveData<String> getPanelsDrawn() {
        return panelsDrawn;
    }

    public LiveData<String> getReactionsReceived() {
        return reactionsReceived;
    }

    public LiveData<String> getStreak() {
        return streak;
    }

    public LiveData<String> getRanking() {
        return ranking;
    }

    public void logout() {
        sessionManager.logout();
    }
}
