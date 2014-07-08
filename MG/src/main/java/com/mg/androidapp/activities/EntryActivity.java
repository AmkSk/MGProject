package com.mg.androidapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dd.plist.NSArray;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.common.Constants;
import com.mg.androidapp.utility.GalleryAdapter;
import com.mg.androidapp.utility.PlistParser;
import com.mg.objects.Actuality;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.Image;
import com.mg.objects.MGEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.List;

public class EntryActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    // =============================================================================
    // Fields
    // =============================================================================

    private AppManager appManager;
    private MGEntry entry;

    private ViewPager gallery;
    private GalleryAdapter adapter;

    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        this.entry = appManager.getSelectedEntry();

        gallery = (ViewPager) findViewById(R.id.entry_gallery);
        adapter = new GalleryAdapter(this);
        gallery.setAdapter(adapter);

        //downloading images to gallery
        this.downloadImages();

        YouTubePlayerView youTubeView = (YouTubePlayerView)
                findViewById(R.id.entry_youtube_view);
        if (this.entry.getKind().equals("Exhibition") || this.entry.getKind().equals("Actual")){
            Actuality actuality = (Actuality) entry;
            if (!actuality.getYtLink().equals("")){
                youTubeView.initialize(appManager.getDeveloperKey(), this);
            }
        }

        this.initGUI();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (this.entry.getKind().equals("Exhibition") || this.entry.getKind().equals("Actual")){
            Actuality actuality = (Actuality) entry;
            if (!actuality.getYtLink().equals(""))
                youTubePlayer.cueVideo(actuality.getYtLink());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void downloadImages(){
        final ImageLoader loader = ImageLoader.getInstance();
        if (this.entry.getImageList().isEmpty()){
            gallery.setVisibility(View.GONE);
            return;
        }
        for(final Image image : this.entry.getImageList()){
            gallery.setVisibility(View.VISIBLE);
            appManager.log("Exhibition Image Download", "INFO: Downloading image "+image.getId());
            loader.loadImage(appManager.createImageURI(image.getId()), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    appManager.log("Exhibition Image Download", "INFO: Image "+image.getId()+ " downloaded");
                    addImageToGallery(new BitmapDrawable(getResources(), bitmap));
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }
    }

    private void addImageToGallery(Drawable drawable){
        this.adapter.addDrawable(drawable);
    }

    private void initGUI(){
        TextView headerTextView = (TextView) findViewById(R.id.entry_header);
        TextView infoTextView = (TextView) findViewById(R.id.entry_InfoTextView);
        Button buildingButton = (Button) findViewById(R.id.entry_button_building);

        headerTextView.setTypeface(appManager.getFontBold());

        // Info
        String lineSep = System.getProperty("line.separator");
        String infoText = null;

        if (appManager.getAppLanguage().equals("cs")){
            headerTextView.setText(this.entry.getName());
            infoText = this.entry.getInfo();
//            buildingButton.setText(((Actuality) entry).getBuildingId(0));
        }
        else if (appManager.getAppLanguage().equals("en")){
            headerTextView.setText(this.entry.getEngName());
            infoText = this.entry.getEngInfo();

        }
        if(!((Actuality) entry).getBuildingIdList().isEmpty()){
            buildingButton.setText(((Actuality) entry).getBuildingId(0));
            buildingButton.setVisibility(View.VISIBLE);
        }
        else {
            buildingButton.setVisibility(View.GONE);
        }

        String editedInfo = infoText.replace("\\" + "n", lineSep);
        infoTextView.setText(editedInfo);

        //date
        TextView date = (TextView) findViewById(R.id.exhibitions_dateTextView);

        if (this.entry.getKind().equals("Constant Exhibition") || this.entry.getKind().equals("Actual")){
            date.setVisibility(View.GONE);
        }
        else {
            date.setText(getResources().getString(R.string.since) + " " + appManager.getDateString(((Exhibition)entry).getDateFrom())
                    + " " + getResources().getString(R.string.until) + " " +  appManager.getDateString(((Exhibition)entry).getDateTo()));
        }

        if (((Actuality) entry).getYtLink().equals("")){
            YouTubePlayerView player = (YouTubePlayerView) findViewById(R.id.entry_youtube_view);
            player.setVisibility(View.GONE);
        }
        if (((Actuality) entry).getFbLink().equals("")){
            Button fbLinkButton = (Button) findViewById(R.id.exhibiton_button_linkFb);
            fbLinkButton.setVisibility(View.GONE);
        }
        if (((Actuality) entry).getTwLink().equals("")){
            Button twLinkButton = (Button) findViewById(R.id.entry_button_linkTw);
            twLinkButton.setVisibility(View.GONE);
        }
    }

    //TODO: sharovanie na FB a Twitter
    public void shareFb_onClick(View v){

    }

    public void shareTw_onClick(View v){

    }

    public void linkFb_onClick(View v){
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Intent facebookPage = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://" + ((Actuality) entry).getFbLink()));
            startActivity(facebookPage);
        } catch (Exception e) {
            String fbID[] = ((Actuality) entry).getFbLink().split("/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+fbID[1]));
            startActivity(launchBrowser);
        }
    }

    public void linkTw_onClick(View v){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + ((Actuality) entry).getTwLink())));
        }catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + ((Actuality) entry).getTwLink())));
        }
    }

    public void building_onClick(View v){
        Button buildingButton = (Button) v;
        String buildingId = (String) buildingButton.getText();

        List<? extends MGEntry> buildingList;

        if (appManager.getBuildingList() == null){
            NSArray array = null;
            try {
                array = appManager.getArrayFromFile(getFilesDir().getPath().toString() + "/" + Constants.BUILDINGS);
            } catch (Exception e) {
                e.printStackTrace();
            }

            buildingList = PlistParser.parseBuildingsArray(array);

            appManager.setBuildingList(buildingList);
        }
        else buildingList = appManager.getBuildingList();

        for (MGEntry entry:buildingList){
            Building building = (Building) entry;
            if (building.getName().equals(buildingId)){
                Intent intent = new Intent(getApplicationContext(), BuildingActivity.class);
                appManager.setSelectedBuilding(building);
                startActivity(intent);
                break;
            }
        }
    }

    // =============================================================================
    // Subclasses
    // =============================================================================

}
