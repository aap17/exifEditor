package com.pahomov.exifeditor.lite.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pahomov.exifeditor.lite.Exifs.ExifField;
import com.pahomov.exifeditor.lite.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by grok on 8/30/17.
 */

public class ActionFragment extends MyFragment implements ExifAdapter.AdapterItemListener {

    private static String TAG = "ActionFragment";

    ImageView imagePreview;
    RecyclerView exifRecyclerview;
    ExifAdapter adapter;
    Subscriber<List<ExifField>> subscriberExifs;
    Subscriber<Bitmap> subscriberImage;
    ProgressBar exifProgress;

    List<ExifField> exifData;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.action_layout, container,false);


        exifIsProcessing(true);
        imageThumbnailIsProcessing(true);

        exifProgress = (ProgressBar)view.findViewById(R.id.exifProgress);
        imagePreview = (ImageView)view.findViewById(R.id.imagePreview);
        exifRecyclerview = (RecyclerView)view.findViewById(R.id.exifContainer);
        exifRecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));

        subscriberExifs = new Subscriber<List<ExifField>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                complain(e.getLocalizedMessage());
            }

            @Override
            public void onNext(List<ExifField> list) {
                showExif(list);
                exifIsProcessing(false);
            }
        };
        activity.getExifInfo().subscribe(subscriberExifs);

        subscriberImage = new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                complain(e.getLocalizedMessage());
            }

            @Override
            public void onNext(Bitmap image) {
                showThumbnail(image);
                imageThumbnailIsProcessing(false);
            }
        };
        activity.getPreviewImage()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriberImage);

        return view;
    }

    private void itemEdited(final String data, final int position) {
        activity.modifyElement(exifData.get(position).fieldName, data).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                complain(""+e.getLocalizedMessage());
            }

            @Override
            public void onNext(Boolean result) {
                if (result) {
                    exifData.get(position).fieldData=data;
                    adapter.notifyDataSetChanged();
                } else
                {
                    complain("did not save item");
                }
            }
        });
    }

    private void imageThumbnailIsProcessing(Boolean isProcessing) {

           if (isProcessing) {
                if (imagePreview != null) imagePreview.setVisibility(View.GONE);

           }
            else {
                if (imagePreview != null) imagePreview.setVisibility(View.VISIBLE);
            }

    }

    private void complain(String text) {
        Log.d(TAG, " "+text);
        Toast.makeText(activity.getApplicationContext(), "" + text, Toast.LENGTH_LONG).show();
    }


    private void exifIsProcessing(Boolean isProcessing) {

        if (isProcessing) {
            if (exifProgress != null) exifProgress.setVisibility(View.VISIBLE);
            if (exifRecyclerview !=null ) exifRecyclerview.setVisibility(View.GONE);

        } else {
            if (exifProgress != null) exifProgress.setVisibility(View.GONE);
            if (exifRecyclerview !=null ) exifRecyclerview.setVisibility(View.VISIBLE);
        }

    }


    private void showExif(List<ExifField> fields) {
        exifData = new ArrayList<>();
        exifData.add(new ExifField());
        exifData.addAll(fields);
        adapter = new ExifAdapter(fields,this);
        exifRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        exifIsProcessing(false);
        for (ExifField field : fields) {
            Log.d(TAG," EXIF"+ field.fieldName + " : "+field.fieldData);
        }
    }




    private void showThumbnail(Bitmap bmp) {
        imagePreview.setImageBitmap(bmp);
        imageThumbnailIsProcessing(false);
    }



    @Override
    public void onItemDelete(int position) {
        itemEdited("", position);
    }

    @Override
    public void onItemEdit(final int position) {

        final Dialog editPopUp = new Dialog(this.getContext());
        editPopUp.setContentView(R.layout.popup_editor);
        final EditText editor = (EditText) editPopUp.findViewById(R.id.popupEditField);
        editor.setText(exifData.get(position).fieldData);
        editPopUp.show();
        editor.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editor, InputMethodManager.SHOW_IMPLICIT);
        TextView okButton = (TextView) editPopUp.findViewById(R.id.editDone);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemEdited(editor.getText().toString(), position);
                editPopUp.hide();
                editPopUp.dismiss();
            }
        });
    }


    @Override
    public void onRunMap(String lat, String longt) {

        activity.showGoogleMap(lat, longt);
    }





}
