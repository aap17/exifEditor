package com.pahomov.exifeditor.lite.Dagger;

import android.app.Application;

/**
 * Created by grok on 9/11/17.
 */

public class DaggerApp extends Application {
    CustomComponent component;


    @Override
    public void onCreate() {
        super.onCreate();
        component= DaggerCustomComponent.builder().moduleExifEditor(new ModuleExifEditor()).moduleFilePathGen(new ModuleFilePathGen(this))
                .build();

    }


    public CustomComponent getComponent()
    {
        return component;
    }
}