package com.yomi.domain.usecases;

import com.yomi.database.StoryEntity;
import com.yomi.repository.StoryRepository;

public class CreateStoryUseCase {
    private final StoryRepository repository;

    public CreateStoryUseCase(StoryRepository repository) {
        this.repository = repository;
    }

    public void execute(StoryEntity story) {
        repository.insertStory(story);
    }
}
