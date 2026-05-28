package com.yomi.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PanelEntity;
import com.yomi.database.StoryEntity;
import com.yomi.repository.SessionManager;
import com.yomi.repository.YomiRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

public class DrawViewModel extends AndroidViewModel {
    private final YomiRepository repository;
    private final SessionManager sessionManager;
    private final MutableLiveData<String> timerValue = new MutableLiveData<>("13h 42m");
    private final MutableLiveData<PanelEntity> previousPanelData = new MutableLiveData<>();

    public DrawViewModel(@NonNull Application application) {
        super(application);
        repository = new YomiRepository(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<String> getTimerValue() {
        return timerValue;
    }

    public LiveData<PanelEntity> getPreviousPanelData() {
        return previousPanelData;
    }

    public void loadPreviousPanelOnly(long storyId) {
        repository.getStoryById(storyId).observeForever(story -> {
            if (story != null && story.getCurrentPanelIndex() > 0) {
                int prevIndex = story.getCurrentPanelIndex() - 1;
                repository.getPanelsForStory(storyId).observeForever(panels -> {
                    if (panels != null) {
                        for (PanelEntity p : panels) {
                            if (p.getPanelIndex() == prevIndex) {
                                previousPanelData.postValue(p);
                                break;
                            }
                        }
                    }
                });
            }
        });
    }

    public void submitTurn(long storyId, long authorId, Bitmap bitmap, String bubbleText) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Use synchronous fetch to avoid observer leaks
            StoryEntity story = repository.getStoryByIdSync(storyId);
            if (story != null && !"COMPLETED".equals(story.getStatus())) {
                int currentIndex = story.getCurrentPanelIndex();
                String authorName = sessionManager.getPlayerName();
                
                savePanelLocalSync(storyId, authorId, authorName != null ? authorName : "Anonyme", currentIndex, bitmap, bubbleText);
                
                // Advance turn
                int nextIndex = currentIndex + 1;
                story.setCurrentPanelIndex(nextIndex);
                
                // Check if the story is now finished
                if (nextIndex >= story.getTotalPanels()) {
                    story.setStatus("COMPLETED");
                    story.setCompletedAt(System.currentTimeMillis());
                }
                
                repository.updateStory(story);
            }
        });
    }

    private void savePanelLocalSync(long storyId, long authorId, String authorName, int index, Bitmap bitmap, String text) {
        String fileName = "panel_" + storyId + "_" + index + "_" + System.currentTimeMillis() + ".png";
        File file = new File(getApplication().getFilesDir(), fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            PanelEntity entity = new PanelEntity(storyId, authorId, authorName, index, file.getAbsolutePath(), text, false);
            repository.insertPanel(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
