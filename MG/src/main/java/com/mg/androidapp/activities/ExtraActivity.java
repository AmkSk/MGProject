package com.mg.androidapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.youtube.player.YouTubeIntents;
import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;

/**
 * Created by amk on 14.11.2013.
 */
public class ExtraActivity extends Activity{

    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager;

    // =============================================================================
    // Override methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        appManager.changeActivityTitle(this, R.string.extraButtonString);
    }


    // =============================================================================
    // Methods
    // =============================================================================

    public void youtube_onClick(View v){
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            Intent intent = new Intent(YouTubeIntents.createUserIntent(this, "videaMG"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(appManager.getYoutubeURL()));
            startActivity(intent);
        }
    }

    public void fb_onClick(View v){
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent facebookPage = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+appManager.getFbId()));
            startActivity(facebookPage);
        } catch (Exception e) {
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+appManager.getFbId()));
            startActivity(launchBrowser);
        }
    }

    public void biennial_onClick(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(appManager.getBienaleURL()));
        startActivity(intent);
    }

    public void biennialFb_onClick(View v){
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent facebookPage = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+appManager.getBienaleFbId()));
            startActivity(facebookPage);
        } catch (Exception e) {
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+appManager.getBienaleFbId()));
            startActivity(launchBrowser);
        }
    }


    public void calendar_onClick(View v){
        Intent calendarIntent = new Intent(this , CalendarActivity.class);
        calendarIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(calendarIntent);
    }


    public void authors_onClick(View v){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.authors))
                .setMessage(getResources().getString(R.string.authorsAbout))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    // =============================================================================
    // Subclasses
    // =============================================================================

}
