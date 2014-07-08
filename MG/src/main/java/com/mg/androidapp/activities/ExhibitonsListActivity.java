package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;

import java.util.ArrayList;
import java.util.List;

public class ExhibitonsListActivity extends Activity{
    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager;
    List<? extends MGEntry> constantExhibitionsList;
    List<? extends MGEntry> exhibitionsList;
    ProgressBar progressBar;
    ListView listView;
    boolean areExhibitionsConstant;

    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        constantExhibitionsList = new ArrayList<Exhibition>();
        exhibitionsList = new ArrayList<Exhibition>();

        this.areExhibitionsConstant = getIntent().getExtras().getBoolean("constant");

        if (areExhibitionsConstant)
            appManager.changeActivityTitle(this, R.string.permanentExhibitions);
        else appManager.changeActivityTitle(this, R.string.exhibitions);

        try {
            if (areExhibitionsConstant){
                if (appManager.getConstantExhibitionsList() == null){
                    NSArray array = appManager.getArrayFromFile(getFilesDir().getPath().toString() + "/" + Constants.CONSTANT_EXHIBITIONS);
                    // parse the constantExhibitions.plist file and fill the list
                    this.constantExhibitionsList = PlistParser.parseExhibitionArray(array, true);
                    appManager.setConstantExhibitionsList(this.constantExhibitionsList);
                }
                else this.constantExhibitionsList = appManager.getConstantExhibitionsList();
            }
            else {
                if (appManager.getExhibitionsList() == null){
                    NSArray array = appManager.getArrayFromFile(getFilesDir().getPath().toString() + "/" + Constants.EXHIBITIONS);
                    // parse the exhibitions.plist file and fill the list
                    this.exhibitionsList = PlistParser.parseExhibitionArray(array, false);
                    appManager.setExhibitionsList(this.exhibitionsList);
                }
                else this.exhibitionsList = appManager.getExhibitionsList();
            }

            this.progressBar = (ProgressBar) findViewById(R.id.itemslist_progressBar);
            this.progressBar.setVisibility(View.VISIBLE);

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

    /**
     * Sets the onItemClick listener to start new EntryActivity
     */
    private void initListView(){
        this.listView = (ListView)findViewById(R.id.ItemsListView);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Exhibition selectedExhibition = (Exhibition) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                // we set, what exhibition was chosen
                appManager.setSelectedEntry(selectedExhibition);
                startActivity(intent);
            }
        });
    }


    /**
     * Stupidly copies items from the constantExhibitionsList to the array and then fills the adapter
     * with them
     */
    private void fillListView(){

        if (areExhibitionsConstant){
           UniversalAdapter adapter = new UniversalAdapter(this,
                R.layout.list_item_row, (List<MGEntry>) constantExhibitionsList);
            listView.setAdapter(adapter);
        }
        else {
            UniversalAdapter adapter = new UniversalAdapter(this,
                    R.layout.list_item_row, (List<MGEntry>) exhibitionsList);
            listView.setAdapter(adapter);
        }
    }
}
