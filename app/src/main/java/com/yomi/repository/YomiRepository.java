package com.yomi.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.yomi.database.FriendshipEntity;
import com.yomi.database.PanelEntity;
import com.yomi.database.PlayerEntity;
import com.yomi.database.ReactionEntity;
import com.yomi.database.StoryEntity;
import com.yomi.database.StoryWithReactions;
import com.yomi.database.YomiDatabase;
import com.yomi.database.dao.FriendshipDao;
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
    private final FriendshipDao friendshipDao;
    private final ExecutorService executorService;

    public YomiRepository(Application application) {
        YomiDatabase db = YomiDatabase.getInstance(application);
        playerDao = db.playerDao();
        storyDao = db.storyDao();
        panelDao = db.panelDao();
        reactionDao = db.reactionDao();
        friendshipDao = db.friendshipDao();
        executorService = Executors.newFixedThreadPool(4);
    }

    public void insertPlayer(PlayerEntity player, Consumer<Long> callback) {
        executorService.execute(() -> {
            long id = playerDao.insert(player);
            if (callback != null) callback.accept(id);
        });
    }

    public List<PlayerEntity> getAllPlayersSync() {
        return playerDao.getAllPlayersSync();
    }

    public LiveData<List<PlayerEntity>> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    public void getPlayerById(long id, Consumer<PlayerEntity> callback) {
        executorService.execute(() -> {
            PlayerEntity player = playerDao.getById(id);
            if (callback != null) callback.accept(player);
        });
    }

    public void getPlayerByUsername(String username, Consumer<PlayerEntity> callback) {
        executorService.execute(() -> {
            PlayerEntity player = playerDao.getByUsername(username);
            if (callback != null) callback.accept(player);
        });
    }

    public void login(String email, String password, Consumer<PlayerEntity> callback) {
        executorService.execute(() -> {
            PlayerEntity player = playerDao.login(email, password);
            if (callback != null) callback.accept(player);
        });
    }

    public void insertStory(StoryEntity story) {
        executorService.execute(() -> storyDao.insert(story));
    }

    public void updateStory(StoryEntity story) {
        executorService.execute(() -> storyDao.update(story));
    }

    public LiveData<StoryEntity> getStoryById(long id) {
        return storyDao.getStoryById(id);
    }

    public StoryEntity getStoryByIdSync(long id) {
        return storyDao.getStoryByIdSync(id);
    }

    public LiveData<List<StoryEntity>> getActiveStories() {
        return storyDao.getActiveStories();
    }

    public LiveData<List<StoryWithReactions>> getActiveStoriesWithReactions() {
        return storyDao.getActiveStoriesWithReactions();
    }

    public LiveData<List<StoryWithReactions>> getCompletedStoriesWithReactions() {
        return storyDao.getCompletedStoriesWithReactions();
    }

    public LiveData<List<StoryWithReactions>> getAllStoriesWithReactions() {
        return storyDao.getAllStoriesWithReactions();
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

    public LiveData<Integer> getReactionCount(long storyId) {
        return reactionDao.getReactionCount(storyId);
    }

    public LiveData<List<ReactionEntity>> getReactionsForPlayerStories(long playerId) {
        return reactionDao.getReactionsForPlayerStories(playerId);
    }

    public LiveData<Integer> getStoryCountByPlayer(long playerId) {
        return storyDao.getStoryCountByPlayer(playerId);
    }

    // Friendship methods
    public void addFriend(long userId, long friendId) {
        executorService.execute(() -> friendshipDao.insert(new FriendshipEntity(userId, friendId)));
    }

    public void removeFriend(long userId, long friendId) {
        executorService.execute(() -> friendshipDao.delete(new FriendshipEntity(userId, friendId)));
    }

    public LiveData<List<PlayerEntity>> getFriends(long userId) {
        return friendshipDao.getFriends(userId);
    }

    public LiveData<List<PlayerEntity>> getFriendSuggestions(long userId) {
        return friendshipDao.getSuggestions(userId);
    }
}
