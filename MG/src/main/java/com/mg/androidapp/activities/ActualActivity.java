package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.objects.Actuality;
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amk on 3.12.2013.
 */
public class ActualActivity extends Activity{

    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager;
    private List<? extends MGEntry> actList;
    private List<? extends MGEntry> eventList;
    private List<? extends MGEntry> exhibitionList;

    Button actualitiesButton;
    Button exhibitionsButton;
    ProgressBar progressBar;

    boolean isImageDownloadingDone = false;

    // =============================================================================
    // Overriden methods
    // =============================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        actList = new ArrayList<Actuality>();
        eventList = new ArrayList<Actuality>();
        exhibitionList = new ArrayList<Exhibition>();

        this.actualitiesButton = (Button) findViewById(R.id.actual_button_actualities);
        this.exhibitionsButton = (Button) findViewById(R.id.actual_button_exhibitions);
        this.progressBar = (ProgressBar) findViewById(R.id.actual_progressBar);

        actualitiesButton.setVisibility(View.INVISIBLE);
        exhibitionsButton.setVisibility(View.INVISIBLE);

        appManager.changeActivityTitle(this, R.string.actualButtonString);

        actList = appManager.getActList();
        eventList = appManager.getEventList();
        exhibitionList = appManager.getExhibitionsList();

        initImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void initImages(){
        final String actImageId = this.actList.get(0).getImage(0).getId();
        final String exhibitionsImageId = this.exhibitionList.get(0).getImage(0).getId();
        final Drawable right = getResources().getDrawable(R.drawable.show_more);

        final ImageLoader imgLoader = ImageLoader.getInstance();

        // image dimensions in dp
        int width = (int) (getResources().getDimension(R.dimen.list_image_width));
        int height = (int) (getResources().getDimension(R.dimen.list_image_height));

        final ImageSize targetSize = new ImageSize(width, height);

        imgLoader.loadImage(appManager.createImageURI(actImageId), targetSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                actualitiesButton.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(),bitmap), null, right, null);
                actualitiesButton.setVisibility(View.VISIBLE);
                if (isImageDownloadingDone){
                    progressBar.setVisibility(View.GONE);
                    exhibitionsButton.setVisibility(View.VISIBLE);
                }
                else isImageDownloadingDone = true;
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        imgLoader.loadImage(appManager.createImageURI(exhibitionsImageId), targetSize, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                exhibitionsButton.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(),bitmap), null, right, null);
                exhibitionsButton.setVisibility(View.VISIBLE);
                if (isImageDownloadingDone){
                    progressBar.setVisibility(View.GONE);
                    actualitiesButton.setVisibility(View.VISIBLE);
                }
                else isImageDownloadingDone = true;
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void actualities_onClick(View v){
        appManager.log("Activity launch", "INFO: launching ACTUALITY LIST Activity");
        Intent intent = new Intent(this, ActualityListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("constant", false);
        this.startActivity(intent);
    }

    public void exhibitions_onClick(View v){
        appManager.log("Activity launch", "INFO: launching EXHIBITIONS LIST Activity");
        Intent intent = new Intent(this, ExhibitonsListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("constant", false);
        this.startActivity(intent);
    }

    // =============================================================================
    // Subclasses
    // =============================================================================

}
