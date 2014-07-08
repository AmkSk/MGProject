package com.mg.androidapp.interfaces;

import android.graphics.drawable.Drawable;

import java.io.FileNotFoundException;

/**
 * Interface used with AsyncTask, to inform that the image was completed
 */
public interface OnImageDownloaded {
    void onImageDownloaded(Drawable drawable, String id) throws FileNotFoundException;
}
