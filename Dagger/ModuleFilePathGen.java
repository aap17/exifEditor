package com.pahomov.exifeditor.lite.Dagger;

import com.pahomov.exifeditor.lite.FilePath.FilePathGen;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grok on 9/11/17.
 */
@Module
public class ModuleFilePathGen {

    DaggerApp daggerApp;


    public ModuleFilePathGen(DaggerApp daggerApp) {
        this.daggerApp = daggerApp;

    }

    @Provides
    @Singleton
    FilePathGen getExifPresenter()
    {

        return new FilePathGen(daggerApp.getApplicationContext());
    }


}
