package com.pahomov.exifeditor.lite.Dagger;

import com.pahomov.exifeditor.lite.Exifs.ExifEditor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by grok on 9/11/17.
 */

@Module
public class ModuleExifEditor {

    public ModuleExifEditor(){}

    @Provides
    @Singleton
    ExifEditor getNetworkLoader()
    {
        return new ExifEditor();
    }

}
