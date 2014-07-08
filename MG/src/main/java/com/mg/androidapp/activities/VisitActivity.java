package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dd.plist.NSArray;
import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.common.Constants;
import com.mg.androidapp.utility.PlistParser;
import com.mg.androidapp.utility.UniversalAdapter;
import com.mg.objects.Building;
import com.mg.objects.Image;
import com.mg.objects.MGEntry;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amk on 11.9.2013.
 */
public class VisitActivity extends Activity{

    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager;
    private List<? extends MGEntry> buildingList;
    ListView listView;
    ProgressBar progressBar;

    // =============================================================================
    // Overriden Methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        buildingList = new ArrayList<Building>();

        appManager.changeActivityTitle(this, R.string.visitButtonString);
        try {
            if (appManager.getBuildingList() == null){
                NSArray array = appManager.getArrayFromFile(getFilesDir().getPath().toString() + "/" + Constants.BUILDINGS);
                this.buildingList = PlistParser.parseBuildingsArray(array);
                appManager.setBuildingList(this.buildingList);
            }
            else this.buildingList = appManager.getBuildingList();

            // putting "fictional building" "About Gallery" to first position
            List <Building> mBuildingList = new ArrayList<Building>();
            mBuildingList.add(this.createAboutGallery());

            for(MGEntry entry: this.buildingList){
                Building bldng = (Building) entry;
                mBuildingList.add(bldng);
            }

            this.buildingList = mBuildingList;

            this.progressBar = (ProgressBar) findViewById(R.id.itemslist_progressBar);
//            this.progressBar.setVisibility(View.VISIBLE);

            this.initListView();
            this.fillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.listView.setVisibility(View.VISIBLE);
    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void initListView(){
        this.listView = (ListView)findViewById(R.id.ItemsListView);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Building selectedBuilding = (Building) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), BuildingActivity.class);
                appManager.setSelectedBuilding(selectedBuilding);
                startActivity(intent);
                appManager.log("Activity launch", "INFO: Launching ActualActivity");
            }
        });
    }

    private void fillListView(){
        UniversalAdapter adapter = new UniversalAdapter(this,
                R.layout.list_item_row, (List<MGEntry>) buildingList);

        listView.setAdapter(adapter);
    }

    private Building createAboutGallery(){
        Building aboutGallery = new Building();
        aboutGallery.setKind("Building");
        aboutGallery.setName("O galerii");
        aboutGallery.setEngName("About gallery");
        aboutGallery.setAddress("");
        aboutGallery.setColor(Color.BLACK);
        String infoString = getResources().getString(R.string.aboutGalleryString);
        try {
            aboutGallery.setInfo(new String(infoString.getBytes(), "utf-8"));
            aboutGallery.setEngInfo(new String(infoString.getBytes(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        aboutGallery.setGps("");
        aboutGallery.addImage(new Image("1a"));
        aboutGallery.addImage(new Image("1b"));
        aboutGallery.addImage(new Image("1c"));
        aboutGallery.addImage(new Image("2a"));
        aboutGallery.addImage(new Image("2b"));
        aboutGallery.addImage(new Image("2c"));
        aboutGallery.addImage(new Image("3a"));
        aboutGallery.addImage(new Image("3b"));
        aboutGallery.addImage(new Image("3c"));
        aboutGallery.addImage(new Image("4a"));
        aboutGallery.addImage(new Image("4b"));
        aboutGallery.addImage(new Image("4c"));
        aboutGallery.addImage(new Image("5a"));
        aboutGallery.addImage(new Image("5b"));
        aboutGallery.addImage(new Image("5c"));
        return aboutGallery;
    }
}
