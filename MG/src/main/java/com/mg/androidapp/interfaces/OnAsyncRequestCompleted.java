package com.mg.androidapp.interfaces;

import android.graphics.drawable.Drawable;

/**
 * Interface used with AsyncTask, to inform that the task was completed
 */
public interface OnAsyncRequestCompleted {
    void onTaskCompleted(byte bytes[]);
}
