package com.yomi.model;

import com.yomi.database.StoryEntity;
import com.yomi.database.StoryWithReactions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mappers {
    public static Story fromEntity(StoryEntity entity, List<Long> playerOrder) {
        if (entity == null) return null;
        
        List<Long> order = new ArrayList<>();
        if (entity.getPlayerOrder() != null && !entity.getPlayerOrder().isEmpty()) {
            try {
                order = Arrays.stream(entity.getPlayerOrder().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                order = new ArrayList<>();
            }
        }

        return new Story(
                entity.getId(),
                entity.getTitle(),
                entity.getCreatorId(),
                entity.getTotalPanels(),
                entity.getCurrentPanelIndex(),
                StoryStatus.valueOf(entity.getStatus()),
                order,
                new ArrayList<>(), // panels list
                entity.getCreatedAt(),
                entity.getInviteCode(),
                0 // Default reaction count
        );
    }

    public static Story fromWithReactions(StoryWithReactions swr) {
        if (swr == null) return null;
        Story story = fromEntity(swr.story, null);
        return new Story(
                story.getId(),
                story.getTitle(),
                story.getCreatorId(),
                story.getTotalPanels(),
                story.getCurrentPanelIndex(),
                story.getStatus(),
                story.getPlayerOrder(),
                story.getPanels(),
                story.getCreatedAt(),
                story.getInviteCode(),
                swr.reactionCount
        );
    }
}
