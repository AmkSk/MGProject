package com.mg.androidapp.utility;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.interfaces.OnImageDownloaded;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Class used to asynchronously download Images by provided image String identifier
 */
public class ImageDownloader extends AsyncTask<String, Void, Drawable> {
    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager = AppManager.getInstance();
    private OnImageDownloaded listener;
    String imageId = null;

    // =============================================================================
    // Constructors
    // =============================================================================

    public ImageDownloader(OnImageDownloaded listener){
        this.listener = listener;
    }

    // =============================================================================
    // Overriden Methods
    // =============================================================================

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        appManager.log("Downloading image", "INFO: Image " + this.imageId + " was downloaded");
        try {
            if (listener != null){
                this.listener.onImageDownloaded(drawable, this.imageId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // =============================================================================
    // Methods
    // =============================================================================

    protected final Drawable doInBackground(String... imageName){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            this.imageId = imageName[0];
            HttpGet request = new HttpGet(appManager.getImagesURL() + imageId + ".png");
            HttpResponse response = httpClient.execute(request);
            InputStream is = response.getEntity().getContent();

            appManager.log("Downloading image", "INFO: Downloading image: " + imageId + ".png");

            Drawable drawable = Drawable.createFromStream(is, "src");
            return drawable;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
