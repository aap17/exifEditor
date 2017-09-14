package com.pahomov.exifeditor.lite.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pahomov.exifeditor.lite.R;


/**
 * Created by grok on 9/10/17.
 */

public class GoogleMapFragment extends MyFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;


    private LatLng coords;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.google_map_layout, container,false);
    //    double lat = Double.parseDouble(this.getArguments().getString("lat"));
      //  double longt =  Double.parseDouble(this.getArguments().getString("longt"));
        //coords = new LatLng(lat, longt);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);


//        mMap.addMarker(new MarkerOptions().position(coords).title(activity.getString(R.string.map_coord)));
  //      mMap.moveCamera(CameraUpdateFactory.newLatLng(coords));



    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        coords = latLng;
        mMap.addMarker(new MarkerOptions().position(latLng).title(activity.getString(R.string.map_coord_custom)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onDetach() {
        if (coords != null) {
            activity.onGPSCoordinatesSet(coords);
        }
        super.onDetach();

    }
}
