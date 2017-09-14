package com.pahomov.exifeditor.lite.FilePath;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by grok on 9/10/17.
 */

public class FilePathGen {
    private Context ctx;
    private String TAG = "presenter";
    private FilePathExtractor extractor;
    private List<String> files;


    public FilePathGen(Context ctx) {
        this.ctx = ctx;
        extractor = new FilePathExtractor();
        files = new ArrayList<>();
    }

    public Single onActivityResult(final Intent data) {

        if ((data.getClipData()!= null) && (data.getClipData().getItemCount() > 1)) {


            Single<List<String>> multiUriProducer = Single.fromCallable(new Callable<List<String>>() {

                @Override
                public List<String> call() throws Exception {

                    return generateUri(data.getClipData());
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());


         return multiUriProducer;

        } else if (data.getData() != null) {

            Single<List<String>> singleUriProducer = Single.fromCallable(new Callable<List<String>>() {

                @Override
                public List<String> call() throws Exception {
                    files = new ArrayList<>();
                    files.add(extractor.getPath(ctx, data.getData()));

                    return files;
                }}).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
            return singleUriProducer;
        }
        return null;
    }

    private List<String> generateUri(ClipData data) {
        FilePathExtractor extractor = new FilePathExtractor();

       files = new ArrayList<>();
        for (int i = 0; i < data.getItemCount(); i++) {
            files.add(extractor.getPath(ctx,data.getItemAt(i).getUri()));

        }
       return files;
    }

    public List<String> getFiles()
    {
        return files;
    }



}







    



