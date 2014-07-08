package com.mg.androidapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.common.VerticalOnlyScrollView;
import com.mg.androidapp.utility.GalleryAdapter;
import com.mg.androidapp.utility.UniversalAdapter;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.Image;
import com.mg.objects.MGEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class BuildingActivity extends FragmentActivity {

    // =============================================================================
    // Fields
    // =============================================================================

    private Building building;      // current opened building
    private AppManager appManager;

    private GoogleMap mMap;

    private ViewPager gallery;
    private GalleryAdapter adapter;

    private VerticalOnlyScrollView scrollView;

    List<MGEntry> exhibitionList;


    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);
        appManager.changeActivityTitle(this, R.string.visitButtonString);

        exhibitionList = new ArrayList<MGEntry>();

        this.building = appManager.getSelectedBuilding();

        if (appManager.getAppLanguage().equals("cs")){
            appManager.addToTitle(this, building.getName());
        }
        else if (appManager.getAppLanguage().equals("en")){
            appManager.addToTitle(this, building.getEngName());
        }

        // map initialization
        if (building.getGps().equals("")){  //there are no GPS coordinates
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.building_mapLayout);
            layout.setVisibility(View.GONE);
        }
        else {
            initMap();
        }



        // building information initialization
        initBuildingInfo();

        gallery = (ViewPager) findViewById(R.id.building_gallery);
        adapter = new GalleryAdapter(this);
        gallery.setAdapter(adapter);

        scrollView = (VerticalOnlyScrollView) findViewById(R.id.entry_scrollView);

        //downloading images to gallery
        this.downloadImages();
        this.initListView();
    }

    // =============================================================================
    // Methods
    // =============================================================================


    private void downloadImages(){
        final ImageLoader loader = ImageLoader.getInstance();
        for(final Image image : this.building.getImageList()){
            appManager.log("Building Image Download", "INFO: Downloading image "+image.getId());
            loader.loadImage(appManager.createImageURI(image.getId()), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    appManager.log("Building Image Download", "INFO: Image "+image.getId()+ " downloaded");
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

    /**
     * Initializes the GUI elements, that show informations about the building
     * Sets fonts ... or would, if they worked TODO: fonts
     */
    private void initBuildingInfo(){
        TextView info = (TextView) findViewById(R.id.buildingInfoTextView);
        //info.setTypeface(appManager.getFontBold());
        String lineSep = System.getProperty("line.separator");
        String infoText = null;

        if (appManager.getAppLanguage().equals("cs")){
            infoText = building.getInfo();
        }
        else if (appManager.getAppLanguage().equals("en")){
            infoText = building.getEngInfo();
        }

        String editedInfo = infoText.replace("\\" + "n", lineSep);
        info.setText(editedInfo);
    }

    /**
     * Intializes the Google Map Fragment
     * Sets location on the map based on the buildings GPS attribute
     */
    private void initMap(){
        this.mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapContainer)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String[] GPS = this.building.getGps().split("_");

        double lat = Double.parseDouble(GPS[0]);
        double lng= Double.parseDouble(GPS[1]);

        LatLng position = new LatLng(lat, lng);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        CameraUpdate center = CameraUpdateFactory.newLatLng(position);
        this.mMap.moveCamera(center);
        this.mMap.animateCamera(zoom);
        this.mMap.addMarker(new MarkerOptions().position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.indicator)));

        ImageView imageView = (ImageView) findViewById(R.id.transparentImage);

        // in order to be able to scroll down the scrollview of the building,
        // we have to catch the touch events and allow/disallow them
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });
    }

    /**
     * Intializes the Listview, that holds permanent exhibitions and exhibitions of the building
     */
    private void initListView(){
        if (building.getName().equals("O galerii")){
            return;
        }


        //iterating through exhibitions
        for (MGEntry entry:appManager.getExhibitionsList()){
            Exhibition exhibition = (Exhibition) entry;
            if (exhibition.getBuildingId(0).equals(this.building.getName())){
                this.exhibitionList.add(entry);
            }
        }

        //iterating through constant exhibitions
        for (MGEntry entry:appManager.getConstantExhibitionsList()){
            Exhibition exhibition = (Exhibition) entry;
            if (exhibition.getBuildingId(0).equals(this.building.getName())){
                this.exhibitionList.add(entry);
            }
        }

        if (!this.exhibitionList.isEmpty()) {
            UniversalAdapter adapter = new UniversalAdapter(this,
                    R.layout.list_item_row, exhibitionList);
            LinearLayout layout = (LinearLayout) findViewById(R.id.building_exhibitionLayout);


            for (int i = 0; i < exhibitionList.size(); i++){
                View view = adapter.getView(i, null, layout);
                view.setBackgroundResource(R.drawable.gray_button);
                final int index = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appManager.setSelectedEntry(exhibitionList.get(index));
                        Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                        startActivity(intent);
                    }
                });
                layout.addView(view);
            }
        }
    }
}
