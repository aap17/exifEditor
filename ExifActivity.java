package com.pahomov.exifeditor.lite;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.pahomov.exifeditor.lite.Dagger.DaggerApp;
import com.pahomov.exifeditor.lite.Exifs.ExifEditor;
import com.pahomov.exifeditor.lite.FilePath.FilePathGen;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by grok on 9/11/17.
 */

public abstract class ExifActivity extends AppCompatActivity {

    @Inject
    ExifEditor exifEditor;
    @Inject
    FilePathGen presenter;

    static final int SELECT_PHOTO_INTENT_ID = 31338;

    public abstract void onMultipleImagesPicked(List<String> filepathes);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DaggerApp)getApplication()).getComponent().inject(this);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermissions()) {
                requestForSpecificPermissions();
            }
        }
    }

    private boolean checkIfAlreadyhavePermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Boolean isGranted = true;
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
            {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    private void requestForSpecificPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }


    public int getSELECT_INTENT_ID() { return SELECT_PHOTO_INTENT_ID; }

}
