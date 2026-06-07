package com.yomi.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.yomi.database.StoryWithReactions;
import com.yomi.model.Mappers;
import com.yomi.model.Story;
import com.yomi.repository.YomiRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyBdsViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final LiveData<List<Story>> allStories;

    public MyBdsViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        allStories = Transformations.map(repository.getAllStoriesWithReactions(), entities -> {
            if (entities == null) return new ArrayList<>();
            return entities.stream().map(Mappers::fromWithReactions).collect(Collectors.toList());
        });
    }

    public LiveData<List<Story>> getAllStories() {
        return allStories;
    }
}
