package com.yomi.ui.canvas;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.yomi.database.PanelEntity;
import com.yomi.domain.usecases.SubmitPanelUseCase;
import com.yomi.repository.SessionManager;
import com.yomi.repository.StoryRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CanvasViewModel extends AndroidViewModel {
    private final SubmitPanelUseCase submitPanelUseCase;
    private final SessionManager sessionManager;
    private final StoryRepository repository;
    private final MutableLiveData<String> timerValue = new MutableLiveData<>("13h 42m");
    private final MutableLiveData<PanelEntity> previousPanelData = new MutableLiveData<>();

    public CanvasViewModel(@NonNull Application application) {
        super(application);
        this.repository = new StoryRepository(application);
        this.submitPanelUseCase = new SubmitPanelUseCase(repository);
        this.sessionManager = new SessionManager(application);
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
        String authorName = sessionManager.getPlayerName();
        new Thread(() -> {
            com.yomi.database.StoryEntity story = repository.getStoryByIdSync(storyId);
            if (story != null) {
                int index = story.getCurrentPanelIndex();
                savePanelLocal(storyId, authorId, authorName != null ? authorName : "Anonyme", index, bitmap, bubbleText);
            }
        }).start();
    }

    private void savePanelLocal(long storyId, long authorId, String authorName, int index, Bitmap bitmap, String text) {
        String fileName = "panel_" + storyId + "_" + index + "_" + System.currentTimeMillis() + ".png";
        File file = new File(getApplication().getFilesDir(), fileName);

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            PanelEntity entity = new PanelEntity(storyId, authorId, authorName, index, file.getAbsolutePath(), text, false);
            submitPanelUseCase.execute(storyId, entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
