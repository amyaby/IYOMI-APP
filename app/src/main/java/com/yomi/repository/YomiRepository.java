package com.yomi.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.yomi.database.PanelEntity;
import com.yomi.database.PlayerEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
import com.yomi.database.YomiDatabase;
import com.yomi.database.dao.PanelDao;
import com.yomi.database.dao.PlayerDao;
import com.yomi.database.dao.ReactionDao;
import com.yomi.database.dao.StoryDao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class YomiRepository {
    private final PlayerDao playerDao;
    private final StoryDao storyDao;
    private final PanelDao panelDao;
    private final ReactionDao reactionDao;
    private final ExecutorService executorService;

    public YomiRepository(Application application) {
        YomiDatabase db = YomiDatabase.getInstance(application);
        playerDao = db.playerDao();
        storyDao = db.storyDao();
        panelDao = db.panelDao();
        reactionDao = db.reactionDao();
        executorService = Executors.newFixedThreadPool(4);
    }

    public void insertPlayer(PlayerEntity player, Consumer<Long> callback) {
        executorService.execute(() -> {
            long id = playerDao.insert(player);
            if (callback != null) callback.accept(id);
        });
    }

    public void login(String email, String password, Consumer<PlayerEntity> callback) {
        executorService.execute(() -> {
            PlayerEntity player = playerDao.login(email, password);
            if (callback != null) callback.accept(player);
        });
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    public void insertStory(StoryEntity story) {
        executorService.execute(() -> storyDao.insert(story));
    }

    public void updateStory(StoryEntity story) {
        executorService.execute(() -> storyDao.update(story));
    }

    public StoryEntity getStoryByIdSync(long id) {
        return storyDao.getStoryByIdSync(id);
    }

    public LiveData<List<StoryEntity>> getAllStories() {
        return storyDao.getAllStories();
    }

    public LiveData<List<StoryEntity>> getActiveStories() {
        return storyDao.getActiveStories();
    }

    public LiveData<List<StoryEntity>> getActiveStoriesForPlayer(long playerId) {
        return storyDao.getActiveStoriesForPlayer(playerId);
    }

    public LiveData<List<StoryEntity>> getCompletedStories() {
        return storyDao.getCompletedStories();
    }

    public void joinStory(long storyId, long playerId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            StoryEntity story = storyDao.getStoryByIdSync(storyId);
            if (story != null) {
                String order = story.getPlayerOrder();
                if (order == null || order.isEmpty()) {
                    order = String.valueOf(playerId);
                } else if (!order.contains(String.valueOf(playerId))) {
                    order += "," + playerId;
                }
                story.setPlayerOrder(order);
                storyDao.update(story);
                if (callback != null) callback.accept(true);
            } else if (callback != null) {
                callback.accept(false);
            }
        });
    }

    public void insertPanel(PanelEntity panel) {
        executorService.execute(() -> panelDao.insert(panel));
    }

    public LiveData<List<PanelEntity>> getPanelsForStory(long storyId) {
        return panelDao.getPanelsForStory(storyId);
    }

    public void insertReaction(ReactionEntity reaction) {
        executorService.execute(() -> reactionDao.insert(reaction));
    }

    public LiveData<List<ReactionEntity>> getReactionsForStory(long storyId) {
        return reactionDao.getReactionsForStory(storyId);
    }

    public LiveData<Integer> getStoryCountByPlayer(long playerId) {
        return storyDao.getStoryCountByPlayer(playerId);
    }

    public LiveData<StoryEntity> getStoryById(long id) {
        return storyDao.getStoryById(id);
    }
}
