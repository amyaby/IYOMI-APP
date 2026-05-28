package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.yomi.repository.YomiRepository;

public class SplashViewModel extends AndroidViewModel {
    private YomiRepository repository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
    }
    
    // Logic for checking login state could go here later
}
