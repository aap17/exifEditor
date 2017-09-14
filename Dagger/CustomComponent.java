package com.pahomov.exifeditor.lite.Dagger;

import com.pahomov.exifeditor.lite.ExifActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by grok on 9/11/17.
 */


@Singleton
@Component(modules = {ModuleExifEditor.class, ModuleFilePathGen.class})
public interface CustomComponent {

    void inject(ExifActivity activity);

}