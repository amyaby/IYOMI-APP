package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.yomi.database.StoryEntity;
import com.yomi.repository.YomiRepository;
import java.util.List;

public class MyBdsViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final LiveData<List<StoryEntity>> allStories;

    public MyBdsViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        allStories = repository.getAllStories();
    }

    public LiveData<List<StoryEntity>> getAllStories() {
        return allStories;
    }
}
