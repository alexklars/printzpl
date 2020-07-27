package com.alex.print;

import android.app.Application;

import com.alex.print.repository.DataRepository;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getApplicationContext());
    }
}
