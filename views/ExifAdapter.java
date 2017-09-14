package com.pahomov.exifeditor.lite.views;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pahomov.exifeditor.lite.Exifs.ExifField;
import com.pahomov.exifeditor.lite.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grok on 8/31/17.
 */

public class ExifAdapter extends RecyclerView.Adapter<ExifAdapter.ExifUIElements>   {



    public interface AdapterItemListener {
        void onItemDelete(int position);
        void onItemEdit(int position);
        void onRunMap(String lat, String longt);
    }


    private List<ExifField> exifList;
    private int GPS_ITEM_COUNT = 8;
    private final int LAYOUT_TOP = 0;
    private final int LAYOUT_GPS = 1;
    private final int LAYOUT_REGULAR = 2;
    private final int EXIF_LATTITUDE_POS = 1;
    private final int EXIF_LONGT_POS = 2;



    private AdapterItemListener adapterItemListener;

    public ExifAdapter(List<ExifField> list, AdapterItemListener listener)
    {
        exifList = new ArrayList<ExifField>();
        ExifField gpsTitle = new ExifField();
        exifList.add(gpsTitle);
        if (list!=null) {
            this.exifList.addAll(list);
            this.adapterItemListener = listener;
        }



    }

    public class ExifUIElements extends RecyclerView.ViewHolder {

        TextView fieldName;
        TextView fieldData;
        ImageView fieldDelete;
        ImageView fieldWantDelete;
        TextView gpsTitle;

        public ExifUIElements(View itemView) {
            super(itemView);
            fieldName = (TextView) itemView.findViewById(R.id.fieldName);
            fieldData = (TextView) itemView.findViewById(R.id.fieldData);
            fieldDelete = (ImageView) itemView.findViewById(R.id.fieldDelete);
            fieldWantDelete = (ImageView) itemView.findViewById(R.id.wantDelete);
            gpsTitle = (TextView) itemView.findViewById(R.id.gpsTitle);
        }
    }

    @Override
    public void onBindViewHolder(final ExifUIElements holder, final int position) {
        ExifField exifField = exifList.get(position);
        //just show title
        if (position == 0) {
            holder.gpsTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exifList.get(EXIF_LATTITUDE_POS).fieldData == null) {
                        exifList.get(EXIF_LATTITUDE_POS).fieldData = "0";
                    }
                    if (exifList.get(EXIF_LONGT_POS).fieldData == null) {
                        exifList.get(EXIF_LONGT_POS).fieldData = "0";
                    }
                 //   adapterItemListener.onRunMap(exifList.get(EXIF_LATTITUDE_POS).fieldData, exifList.get(EXIF_LONGT_POS).fieldData);
                }
            });
          return;
        }
        holder.fieldName.setText(exifField.fieldName);
        holder.fieldData.setText(exifField.fieldData);
        holder.fieldDelete.setVisibility(View.GONE);
        holder.fieldWantDelete.setVisibility(View.VISIBLE);

        holder.fieldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemListener.onItemEdit(position);
            }
        });

        holder.fieldWantDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fieldDelete.setVisibility(View.VISIBLE);
                holder.fieldWantDelete.setVisibility(View.GONE);
            }
        });
        holder.fieldDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemListener.onItemDelete(position);
            }
        });


    }

    @Override
    public ExifUIElements onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        switch (viewType){
            case LAYOUT_TOP:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exif_gps_title, parent, false);
                break;
            case LAYOUT_GPS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exif_gps_field, parent, false);
                break;
            case LAYOUT_REGULAR:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exif_all, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exif_all, parent, false);
                break;
                    }
        ExifUIElements exifUIElements = new ExifUIElements(v);

        return exifUIElements;
    }


    @Override
    public int getItemCount() {
        return exifList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
        {
            return LAYOUT_TOP;
        } else if ((position > 0)&& (position < GPS_ITEM_COUNT + 1)) {
            return LAYOUT_GPS;
        }

        return LAYOUT_REGULAR;
    }

    @Override
    public boolean onFailedToRecycleView(ExifUIElements holder) {
        return true;
    }




}
