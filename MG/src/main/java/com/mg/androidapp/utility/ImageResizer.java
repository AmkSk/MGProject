package com.mg.androidapp.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.mg.androidapp.interfaces.OnImageResized;

/**
 * Created by amk on 18.11.2013.
 */
public class ImageResizer extends AsyncTask<Drawable, Void, Drawable> {

    Context context;
    Drawable drawable;
    String drawableId;
    OnImageResized listener;
    int newHeight = 100;

    public ImageResizer(Context context, String drawableId, OnImageResized listener) {
        this.context = context;
        this.listener = listener;
        this.drawableId = drawableId;
    }

    public ImageResizer(Context context, int height){
        this.context = context;
        this.newHeight = height;
    }

    public ImageResizer(Context context, OnImageResized listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Drawable doInBackground(Drawable... drawables) {
        Drawable drawable = drawables[0];
        Bitmap b = ((BitmapDrawable)drawable).getBitmap();

        int width = b.getWidth();
        int height = b.getHeight();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b,(int) (newHeight*density), (int) ((newHeight*density*height)/(width)),false);
        drawable = new BitmapDrawable(Resources.getSystem(), bitmapResized);
        this.drawable = drawable;
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);

        if (this.listener != null){
            this.listener.OnImageResized(drawable, drawableId);
        }
    }
}
