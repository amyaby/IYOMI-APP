package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PlayerEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;

public class AuthViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void login(String email, String password) {
        repository.login(email, password, player -> {
            if (player != null) {
                sessionManager.createSession(player.getId(), player.getName());
                loginSuccess.postValue(true);
            } else {
                error.postValue("Identifiants incorrects");
            }
        });
    }

    public void register(String username, String email, String password) {
        PlayerEntity player = new PlayerEntity(username, email, password, "👤", "#7B5EA7");
        repository.insertPlayer(player, id -> {
            if (id > 0) {
                sessionManager.createSession(id, username);
                loginSuccess.postValue(true);
            } else {
                error.postValue("Erreur lors de l'inscription");
            }
        });
    }
}
