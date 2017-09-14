package com.pahomov.exifeditor.lite.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pahomov.exifeditor.lite.R;


/**
 * Created by grok on 8/30/17.
 */

public class WelcomeFragment extends MyFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.welcome_fragment, container,false);
        ImageView chooser2 = (ImageView) view.findViewById(R.id.pickMultiple);
        chooser2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

          /*  case R.id.pickSingle:
                activity.startActivityForResult(photoPickerIntent, activity.getSELECT_FOTO_INTENT());
                break;
*/
            case R.id.pickMultiple:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                activity.startActivityForResult(Intent.createChooser(photoPickerIntent, "get files"), activity.getSELECT_INTENT_ID());
                break;
        }

    }


}
