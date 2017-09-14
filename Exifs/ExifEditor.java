package com.pahomov.exifeditor.lite.Exifs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.pahomov.exifeditor.lite.Dagger.BitmapPicker;
import com.pahomov.exifeditor.lite.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grok on 8/30/17.
 */

public class ExifEditor {

    private String TAG = "ExifEditor";

    private ExifInterface exifInterface;
    private List <ExifInterface> exifInterfaceList;



    private List<String> filepathes;



    private boolean isSingleFile = true;


    private String[] exifTags = {
            ExifInterface.TAG_GPS_LATITUDE,  ExifInterface.TAG_GPS_LONGITUDE,
            ExifInterface.TAG_GPS_ALTITUDE_REF, ExifInterface.TAG_GPS_DATESTAMP,
            ExifInterface.TAG_GPS_LATITUDE_REF, ExifInterface.TAG_GPS_LONGITUDE_REF,
            ExifInterface.TAG_GPS_PROCESSING_METHOD,ExifInterface.TAG_GPS_TIMESTAMP,
            ExifInterface.TAG_MAKE, ExifInterface.TAG_MODEL,
            ExifInterface.TAG_F_NUMBER,ExifInterface.TAG_DATETIME,
            ExifInterface.TAG_EXPOSURE_TIME, ExifInterface.TAG_FLASH,
            ExifInterface.TAG_FOCAL_LENGTH,ExifInterface.TAG_GPS_ALTITUDE,
            ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.TAG_IMAGE_WIDTH,
            ExifInterface.TAG_ISO_SPEED_RATINGS,  ExifInterface.TAG_ORIENTATION,
            ExifInterface.TAG_WHITE_BALANCE
    };

    public ExifEditor() {

    }

   private Observable getMultipleFilesInfo(final List<String> fileList) {
      isSingleFile = false;
       Observable exifProducer = Observable.create(new Observable.OnSubscribe<List<ExifField>>() {
        @Override
           public void call(Subscriber<? super List<ExifField>> subscriber) {
               subscriber.onNext(prepareMultipleExifs(fileList));
               subscriber.onCompleted();
           }
       }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

       return exifProducer;
   }

   public Observable getFileInfo(List<String> fileList) {

       if (fileList.size() == 1) {
           return getSingleFileInfo(fileList.get(0));
       }
       return null;// getMultipleFilesInfo(fileList);
   }

    private Observable getSingleFileInfo(String string) {
        Log.d("LAL", "here");
        try {
            isSingleFile = true;
            filepathes = new ArrayList<>();
            filepathes.add(string);
            exifInterface = new ExifInterface(filepathes.get(0));

            Observable exifProducer = Observable.create(new Observable.OnSubscribe<List<ExifField>>() {
                @Override
                public void call(Subscriber<? super List<ExifField>> subscriber) {
                    subscriber.onNext(prepareSingleExif(exifInterface));

                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());




            return exifProducer;

        } catch (IOException e) {
            e.printStackTrace();
        }
      return null;
    }



    private List<ExifField> prepareMultipleExifs(List<String> list) {
        filepathes = new ArrayList<>();
        exifInterfaceList = new ArrayList<ExifInterface>();
        for (String item : list) {
            try {
                exifInterfaceList.add(new ExifInterface(item));
                filepathes.add(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<List<ExifField>> exifDatas = new ArrayList<List<ExifField>>();
        for (ExifInterface exifElement : exifInterfaceList) {
            exifDatas.add(prepareSingleExif(exifElement));
        }
        List<ExifField> dataToDisplay = new ArrayList<ExifField>();

        for (int k = 0; k < exifDatas.get(0).size(); k++) {
            boolean theSame = true;
            for (int i = 0; i < exifDatas.size(); i++) {




                if ((exifDatas.get(i).get(k).fieldData == null)||
                    (exifDatas.get(0).get(k).fieldData == null)||
                    (!exifDatas.get(0).get(k).fieldData.contentEquals(exifDatas.get(i).get(k).fieldData))) {
                    theSame = false;
                    break;
                }
            }
            if (theSame) {
                dataToDisplay.add(exifDatas.get(0).get(k));
            } else {
                ExifField blankField = new ExifField();
                blankField.fieldData = "";
                blankField.fieldName = exifDatas.get(0).get(k).fieldName;
                dataToDisplay.add(blankField);
            }
        }
        return  dataToDisplay;
    }


    private List<ExifField> prepareSingleExif(ExifInterface exifInterface) {

        List<ExifField> fields = new ArrayList<>();
        for (String name : exifTags) {
            ExifField field = new ExifField();
            field.fieldName = name;
            field.fieldData = exifInterface.getAttribute(name);
            fields.add(field);
        }
        return  fields;
    }


    private boolean modifyExifData(String fieldName, String data) {

        if (isSingleFile) {
            return modifySingleElement(filepathes.get(0), exifInterface, fieldName, data);
        } else {
            return modifyMultipleElements(fieldName, data);
        }

    }

    private boolean modifySingleElement(String filepath, ExifInterface exifInterface, String fieldName, String data) {
        try {
            exifInterface.setAttribute(fieldName, data);
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, ""+e.getLocalizedMessage());
        }

        return isElementChanged(filepath, fieldName, data);
    }




    private boolean isElementChanged(String filepath, String fieldName, String newData) {
        try {
            ExifInterface newExifInt = new ExifInterface(filepath);
            String checkingData = newExifInt.getAttribute(fieldName);
            if (newData.contentEquals("") && checkingData == null) {
                return true;
            } else if  (checkingData.contentEquals(newData)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean modifyMultipleElements(String fieldName, String data) {
        boolean isOk = true;
        for (int i = 0; i < exifInterfaceList.size(); i++) {
            if (!modifySingleElement(filepathes.get(i), exifInterfaceList.get(i), fieldName, data)){
                isOk = false;
            }
        }
        return isOk;
    }



    public Observable modifyElement(final String fieldName, final String data) {

        Observable producer = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(modifyExifData(fieldName, data));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        return producer;
    }

    public Observable getPreview(final Context ctx) {


        Observable bitmapObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(bitmapGenerator(ctx));

            }
        });//.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        return bitmapObservable;
    }

    private Bitmap bitmapGenerator(Context ctx) {
        if (isSingleFile) {
            BitmapPicker picker = new BitmapPicker();
            int width = getImageSize(ctx);
            return picker.getBitmap(ctx, Uri.fromFile(new File(filepathes.get(0))), width, width);
        }
        return BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher_round);

    }

    private int getImageSize(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        return (int)(outSize.x*0.3);
    }





}
