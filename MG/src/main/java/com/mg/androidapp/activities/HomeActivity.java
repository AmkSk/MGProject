package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.common.Constants;
import com.mg.androidapp.interfaces.OnActualityChanged;
import com.mg.objects.Actuality;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The first activity after Splashscreen
 * - Cycles through Exhibitions, Actualities, Events and Buildings and shows relevant info about them
 */
public class HomeActivity extends Activity implements OnActualityChanged {

    // =============================================================================
    // Fields
    // =============================================================================

    private AppManager appManager;
    private int entryIndex = 0;
    private List<MGEntry> entryList;

    ImageLoader imgLoader;

    private Animation fadeIn = null;
    private Animation fadeOut = null;

    // UI items
    private ImageView imageView;
    private RelativeLayout eventLayout;
    TextView row1;
    TextView row2;
    TextView row3;

    Map<String, Bitmap> imageMap;


    // =============================================================================
    // Overriden Methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        appManager.changeActivityTitle(this, R.string.app_name);

        entryList = new ArrayList<MGEntry>();
        imageMap = new HashMap<String, Bitmap>();

        this.initUI();
        this.createAnimations();

        entryList.addAll(appManager.getActList());
        entryList.addAll(appManager.getExhibitionsList());
        entryList.addAll(appManager.getEventList());
        entryList.addAll(appManager.getBuildingList());

        this.downloadImages();
        this.nextEntry();
    }

    @Override
    public void OnActualityChanged() {
        if (entryIndex == entryList.size()-1)
            entryIndex = 0;
        else entryIndex++;

        nextEntry();
    }

    // =============================================================================
    //  Methods
    // =============================================================================

    private void initUI(){
        this.row1 = (TextView) findViewById(R.id.home_text_row1);
        this.row2 = (TextView) findViewById(R.id.home_text_row2);
        this.row3 = (TextView) findViewById(R.id.home_text_row3);

//        if (appManager.getFont() != null){
//            row2.setTypeface(appManager.getFont());
//            row3.setTypeface(appManager.getFont());
//        }

        if (appManager.getFontBold() != null){
            row1.setTypeface(appManager.getFontBold());
        }

        this.imageView = (ImageView) findViewById(R.id.home_imageView);
        imageView.setDrawingCacheEnabled(false);

        this.eventLayout = (RelativeLayout) findViewById(R.id.home_eventLayout);
    }

    /**
     * Creates and initializes fadein and fadeout animations
     * In the listeners methods are called to loop through the actualities/exhibitions/buildings
     */
    private void createAnimations(){
        this.fadeIn = new AlphaAnimation(0, 1);
        this.fadeIn.setInterpolator(new DecelerateInterpolator());
        this.fadeIn.setDuration(Constants.FADEIN);
        this.fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                animateFadeOut();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        this.fadeOut = new AlphaAnimation(1, 0);
        this.fadeOut.setInterpolator(new AccelerateInterpolator());
        this.fadeOut.setStartOffset(Constants.ANIMATION_OFFSET);
        this.fadeOut.setDuration(Constants.FADEOUT);
        this.fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                OnActualityChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void downloadImages(){
        for(MGEntry entry : entryList){
            if (entry.getImageList().isEmpty()){
                continue;
            }

            final String imageId = entry.getImage(0).getId();

            appManager.log("Building Image Download", "INFO: Downloading image "+imageId);
            imgLoader = ImageLoader.getInstance();
            imgLoader.loadImage(appManager.createImageURI(imageId), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {}

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    appManager.log("Building Image Download", "INFO: Image " + imageId + " downloaded");
                    String currentActualityImageId = entryList.get(entryIndex).getImage(0).getId();
                    imageMap.put(s, bitmap);
                    currentActualityImageId = appManager.createImageURI(currentActualityImageId);
                    if (currentActualityImageId != null) {
                        if (currentActualityImageId.equals(s)) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {}
            });
        }
    }

    /**
     * Prepares next entry to be shown
     * - Fills the UI with data of the entry
     * - Starts the animation
     */
    private void nextEntry(){
        if (entryList.isEmpty()){
            return;
        }

        if (entryList.get(entryIndex).getImageList().isEmpty()){
           entryIndex++;
           nextEntry();
           return;
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.home_mainLinearLayout);
        layout.setBackgroundResource(R.drawable.gray_button);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MGEntry selectedEntry = entryList.get(entryIndex);
                if (selectedEntry.getKind().equals("Building")){
                    Intent intent = new Intent(getApplicationContext(), BuildingActivity.class);
                    // we set, what building was chosen
                    appManager.setSelectedBuilding((Building)selectedEntry);
                    appManager.log("Starting Activity", "INFO: Starting BUILDING activity");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                    // we set, what exhibition was chosen
                    appManager.setSelectedEntry(selectedEntry);
                    appManager.log("Starting Activity", "INFO: Starting ENTRY activity");
                    startActivity(intent);
                }
            }
        });

        MGEntry currentEntry =  entryList.get(entryIndex);

        Bitmap bitmap = this.imageMap.get(appManager.createImageURI(currentEntry.getImage(0).getId()));
        this.imageView.setImageBitmap(bitmap);

        this.row3.setVisibility(View.VISIBLE);

        if (appManager.getAppLanguage().equals("cs")){
            this.row1.setText(currentEntry.getName());
        }
        else if (appManager.getAppLanguage().equals("en")){
            this.row1.setText(currentEntry.getEngName());
        }

        // buildings have address on the second row, other entries will put building reference here
        if (!currentEntry.getKind().equals("Building")){
            if (!((Actuality)currentEntry).getBuildingIdList().isEmpty()){
                this.row2.setText(((Actuality)currentEntry).getBuildingId(0));
            }
        }

        // only events and exhibitions have date from - date to
        if (currentEntry.getKind().equals("Exhibition") || currentEntry.getKind().equals("Event")){
            Exhibition entry = (Exhibition)currentEntry;
            String dateFrom = appManager.getDateString(entry.getDateFrom());
            String dateTo = appManager.getDateString(entry.getDateTo());

            this.row3.setText(dateFrom + " - " + dateTo);
        }
        if (currentEntry.getKind().equals("Building")){
            this.row2.setText(((Building)currentEntry).getAddress());
            this.row3.setVisibility(View.INVISIBLE);
        }
        if (currentEntry.getKind().equals("Constant Exhibition") || currentEntry.getKind().equals("Constant Exhibition")){
            this.row3.setVisibility(View.INVISIBLE);
        }

        this.animateFadeIn();
    }

    private void animateFadeIn(){
        this.eventLayout.startAnimation(this.fadeIn);
        this.imageView.startAnimation(this.fadeIn);
    }

    private void animateFadeOut(){
        this.eventLayout.startAnimation(this.fadeOut);
        this.imageView.startAnimation(this.fadeOut);
    }
}