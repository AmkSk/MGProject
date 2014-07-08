package com.mg.androidapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.dd.plist.NSDate;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.common.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by amk on 13.9.2013.
 */
public class SplashscreenActivity extends Activity {



    // =============================================================================
    // Fields
    // =============================================================================
    SharedPreferences settings;
    AppManager appManager;

    // =============================================================================
    // Overriden methods
    // =============================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);

        appManager = AppManager.getInstance();
        appManager.setThemeButtonColor(getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorBackground,}).getColor(0, 0xFF00FF));
    }

    @Override
    protected void onStart() {
        super.onResume();
        this.createImgDirs();

        if (isDeviceOnline())
        {
            new PrefetchData().execute();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.dialog_deviceOffline))
                    .setMessage(getResources().getString(R.string.dialog_offlineMessage))
                    .setPositiveButton(getResources().getString(R.string.dialog_retry), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            recreate();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .show();
        }
    }

    // =============================================================================
    // Methods
    // =============================================================================

    /**
     * Method that checks if the device is connected to Internet
     *
     * @return true/false
     */
    public boolean isDeviceOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void setLastChecked(){
        SharedPreferences.Editor editor = settings.edit();

        Calendar c = Calendar.getInstance();

        editor.putLong("lastCheckedTime",c.getTimeInMillis());
        editor.commit();
    }

    private long getLastChecked(){
        return settings.getLong("lastCheckedTime", 0);
    }

    private void createImgDirs(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/MG/images");
        dir.mkdirs();
    }

    private void fillLists(){
        try{
            String dir = getFilesDir().getPath().toString();
            appManager.parseBuildingPlist(dir);
            appManager.parseActualitiesPlist(dir);
            appManager.parseConstantExhibitionPlist(dir);
            appManager.parseEventPlist(dir);
            appManager.parseExhibitionPlist(dir);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startHomeScreen(){
        Intent intent = new Intent(SplashscreenActivity.this, HomeActivity.class);
        SplashscreenActivity.this.startActivity(intent);
        finish();

    }

    // =============================================================================
    // Subclasses
    // =============================================================================
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (onlineDataChanged()){
                    appManager.log("Updating files", "INFO: online data have changed, proceeding to download plist files");

                    downloadPlist(Constants.ACTUALITIES);
                    downloadPlist(Constants.EVENTS);
                    downloadPlist(Constants.BUILDINGS);
                    downloadPlist(Constants.EXHIBITIONS);
                    downloadPlist(Constants.CONSTANT_EXHIBITIONS);
                }
                else {
                    appManager.log("Updating files", "INFO: online data (Plists) have not changed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.connectionProblem, Toast.LENGTH_LONG).show();
                finish();
            }

            //filling plists
            appManager.log("Filling lists", "INFO: filling lists from plist files");
            fillLists();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startHomeScreen();
        }

        /**
         * Method that a plist file from the server
         * @param name - name of the p list
         * @throws ExecutionException
         * @throws InterruptedException
         * @throws IOException
         */
        private byte[] downloadPlist(String name) throws ExecutionException, InterruptedException, IOException {
            String filePath = getFilesDir().getPath().toString() + "/" + name;

            URL url = new URL(appManager.getServerURL()+name);
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

            byte[] bytes = byteBuffer.toByteArray();
            File file = new File(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bytes);
            bos.flush();
            bos.close();
            appManager.log("","INFO: File " + name + " downloaded to internal memory");
            return bytes;
        }
        /**
         * Checks if online plists are changed
         * @return true/false
         */
        private boolean onlineDataChanged() throws Exception {

            byte[] bytes = downloadPlist(Constants.LAST_CHANGE);
            NSDictionary dict = (NSDictionary) PropertyListParser.parse(bytes);

            NSDate date = (NSDate) dict.objectForKey("Date");

            long lastUpdated = date.getDate().getTime();
            long lastChecked = getLastChecked();

//          appManager.log("update Date", "INFO: Date last UPDATED online: "+lastUpdated);
//          appManager.log("update Date", "INFO: Date last CHECKED: "+ lastChecked);

            if (lastChecked == 0){
                setLastChecked();
                return true;
            }

            if (lastUpdated > lastChecked){
                return true;
            }
            else return false;
        }


    }

}