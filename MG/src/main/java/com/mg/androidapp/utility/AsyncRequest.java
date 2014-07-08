package com.mg.androidapp.utility;

import android.os.AsyncTask;

import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.interfaces.OnAsyncRequestCompleted;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class used to asynchronously download plist files (anything else, too)
 */
public class AsyncRequest extends AsyncTask<String, Void, byte[]> {

    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager = AppManager.getInstance();
    private OnAsyncRequestCompleted listener;

    // =============================================================================
    // Constructor
    // =============================================================================

    public AsyncRequest(OnAsyncRequestCompleted listener){
        this.listener = listener;
    }

    public AsyncRequest(){}

    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        if (listener != null)
            listener.onTaskCompleted(bytes);
    }

    // =============================================================================
    // Methods
    // =============================================================================

    protected byte[] doInBackground(String... plist){
        try {

            URL url = new URL(appManager.getServerURL()+plist[0]);
            URLConnection conn = url.openConnection();

            appManager.log("test", "INFO: Connecting to " + url.getPath());

            InputStream stream = conn.getInputStream();

            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = stream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}