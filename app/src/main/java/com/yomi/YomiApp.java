package com.yomi;

import android.app.Application;
import com.yomi.repository.YomiRepository;

public class YomiApp extends Application {
    private YomiRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new YomiRepository(this);
    }

    public YomiRepository getRepository() {
        return repository;
    }
}
