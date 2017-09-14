package com.pahomov.exifeditor.lite.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pahomov.exifeditor.lite.MainActivity;

/**
 * Created by grok on 8/30/17.
 */

public abstract class MyFragment extends Fragment{


    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;

    }



    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }
}
