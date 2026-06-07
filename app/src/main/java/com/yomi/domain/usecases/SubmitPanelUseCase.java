package com.yomi.domain.usecases;

import com.yomi.database.PanelEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.StoryRepository;
import java.util.Arrays;

public class SubmitPanelUseCase {
    private final StoryRepository repository;

    public SubmitPanelUseCase(StoryRepository repository) {
        this.repository = repository;
    }

    public void execute(long storyId, PanelEntity panel) {
        // Insert the new panel
        repository.insertPanel(panel);
        
        // Update story state and contributors
        new Thread(() -> {
            StoryEntity story = repository.getStoryByIdSync(storyId);
            if (story != null) {
                // Increment panel index
                int nextIndex = story.getCurrentPanelIndex() + 1;
                story.setCurrentPanelIndex(nextIndex);
                
                // Add author to player order if not already present
                String order = story.getPlayerOrder();
                String authorIdStr = String.valueOf(panel.getAuthorId());
                if (order == null || order.isEmpty()) {
                    order = authorIdStr;
                } else {
                    boolean alreadyIn = false;
                    String[] parts = order.split(",");
                    for (String p : parts) {
                        if (p.trim().equals(authorIdStr)) {
                            alreadyIn = true;
                            break;
                        }
                    }
                    if (!alreadyIn) {
                        order += "," + authorIdStr;
                    }
                }
                story.setPlayerOrder(order);
                
                // Mark as completed if reached total panels
                if (nextIndex >= story.getTotalPanels()) {
                    story.setStatus("COMPLETED");
                    story.setCompletedAt(System.currentTimeMillis());
                }

                repository.updateStory(story);
            }
        }).start();
    }
}
