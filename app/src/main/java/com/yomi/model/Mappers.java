package com.yomi.model;

import com.yomi.database.StoryEntity;
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
                entity.getInviteCode()
        );
    }
}
