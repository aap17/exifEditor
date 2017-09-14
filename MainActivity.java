package com.pahomov.exifeditor.lite;

import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.pahomov.exifeditor.lite.Exifs.GPS;
import com.pahomov.exifeditor.lite.views.ActionFragment;
import com.pahomov.exifeditor.lite.views.GoogleMapFragment;
import com.pahomov.exifeditor.lite.views.WelcomeFragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import hotchemi.android.rate.AppRate;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

public class MainActivity extends ExifActivity {


    private String TAG = "MainActivity";
    public static String WELCOME_FRAGMENT_TAG = "WelcomeFragment";
    public static String ACTION_FRAGMENT_TAG = "ActionFragment";
    public static String GOOGLE_MAP_FRAGMENT_TAG = "GoogleMapFragment";

    private WelcomeFragment welcomeFragment;
    private ActionFragment actionFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppRate.showRateDialogIfMeetsConditions(this);
        initFragment(new WelcomeFragment(), WELCOME_FRAGMENT_TAG);




    }

    @Override
    public void onMultipleImagesPicked(List<String> filepathes) {

    }



    private void initFragment(Fragment fragment, String tag) {


        if (findViewById(R.id.frame_container) != null) {

            FragmentManager manager =  getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.addToBackStack(null);
            transaction.add(R.id.frame_container, fragment, tag);
            transaction.commit();
            Log.e("MainActivity", "added new fragment.");
            Log.d(TAG, "backstack:"  + getFragmentManager().getBackStackEntryCount());
        } else {
            Log.e("MainActivity", "container null");
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            Log.i("MainActivity", "popping backstack");

            fm.popBackStack();

        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            finish();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {

            case SELECT_PHOTO_INTENT_ID:
                if (resultCode == RESULT_OK) {

                    presenter.onActivityResult(data).subscribe(new SingleSubscriber<List<String>>() {
                        @Override
                        public void onSuccess(List<String> files) {

                            if (getSupportFragmentManager().findFragmentByTag(ACTION_FRAGMENT_TAG) == null) {
                                getSupportFragmentManager()
                                        .beginTransaction().
                                        remove(getSupportFragmentManager().findFragmentByTag(ACTION_FRAGMENT_TAG));
                            }
                                actionFragment = new ActionFragment();
                                initFragment(actionFragment, ACTION_FRAGMENT_TAG);

                        }
                        @Override
                        public void onError(Throwable error) {

                        }
                    });

                }

        }
    }

    public void onEditMultiple(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(photoPickerIntent, "get files"), this.getSELECT_INTENT_ID());

    }


    public Observable getExifInfo() {
        return  exifEditor.getFileInfo(presenter.getFiles());
    }

    public rx.Observable getPreviewImage() {
        return  exifEditor.getPreview(this);
    }


    public void showGoogleMap(String lat, String longt){
        GoogleMapFragment fragment = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lat", lat);
        bundle.putString("longt",longt);

        fragment.setArguments(bundle);
        initFragment(fragment, WELCOME_FRAGMENT_TAG);
    }


    public Observable modifyElement(final String fieldName, final String data) {
        return exifEditor.modifyElement(fieldName, data);
    }

    public void onGPSCoordinatesSet(LatLng coords) {
        exifEditor.modifyElement(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(coords.latitude)).subscribe(new Single.OnSubscribe() {
            @Override
            public void call(Object o) {

            }
        });
        exifEditor.modifyElement(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(coords.latitude)).subscribe(new Single.OnSubscribe() {
            @Override
            public void call(Object o) {

            }
        });
        exifEditor.modifyElement(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(coords.longitude)).subscribe(new Single.OnSubscribe() {
            @Override
            public void call(Object o) {

            }
        });
        exifEditor.modifyElement(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(coords.longitude)).subscribe(new Single.OnSubscribe() {
            @Override
            public void call(Object o) {

            }
        });


    }

}
