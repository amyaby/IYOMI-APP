package com.yomi;

import android.app.Application;
import com.yomi.database.PlayerEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.YomiRepository;
import java.util.List;
import java.util.concurrent.Executors;

public class YomiApp extends Application {
    private YomiRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new YomiRepository(this);
        
        Executors.newSingleThreadExecutor().execute(() -> {
            YomiRepository repo = repository;
            List<PlayerEntity> existing = repo.getAllPlayersSync();
            
            if (existing == null || existing.isEmpty()) {
                // 1. Create Demo Players
                repo.insertPlayer(new PlayerEntity("luna", "luna@yomi.com", "pass", "🌙", "#FDE8F3"), lunaId -> {
                    repo.insertPlayer(new PlayerEntity("arty", "arty@yomi.com", "pass", "🎨", "#EAF3DE"), artyId -> {
                        repo.insertPlayer(new PlayerEntity("foxi", "foxi@yomi.com", "pass", "🦊", "#FFF0E0"), foxiId -> {
                            
                            // 2. Create the Demo Story "L'Alien au café"
                            // You can change this title or the invite code here!
                            StoryEntity demoStory = new StoryEntity("L'Alien au café", lunaId, 4, "ALYN7");
                            demoStory.setStatus("COMPLETED");
                            demoStory.setCompletedAt(System.currentTimeMillis());
                            // Link it to multiple players
                            demoStory.setPlayerOrder(lunaId + "," + artyId + "," + foxiId);
                            repo.insertStory(demoStory);
                        });
                    });
                });
            }
        });
    }

    public YomiRepository getRepository() {
        return repository;
    }
}
