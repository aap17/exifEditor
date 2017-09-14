package com.pahomov.exifeditor.lite.Dagger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by grok on 8/30/17.
 */

public class BitmapPicker {



    public BitmapPicker() {

    }




    public Bitmap getBitmap (Context ctx, Uri uri, int ThumbWidth, int ThumbHeight) {

        Bitmap bitmap = null;
        try {
            InputStream input = ctx.getContentResolver().openInputStream(uri);
            int thumbhail_size = (ThumbWidth > ThumbHeight) ? ThumbWidth : ThumbHeight;
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
                return null;

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > thumbhail_size) ? (originalSize / thumbhail_size) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);

            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            input = ctx.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
        } catch (IOException e)
        {
            Toast.makeText(ctx, " "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }

    private int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

}
