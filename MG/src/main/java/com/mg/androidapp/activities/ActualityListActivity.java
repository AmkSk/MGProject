package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.utility.UniversalAdapter;
import com.mg.objects.MGEntry;

import java.util.List;

/**
 * Contains a simple list of Actualities
 * - uses UniversalAdapter
 */
public class ActualityListActivity extends Activity{
    // =============================================================================
    // Fields
    // =============================================================================
    AppManager appManager;
    ListView listView;
    ProgressBar progressBar;

    private List<? extends MGEntry> actList;
    private List<? extends MGEntry> eventsList;

    // =============================================================================
    // Override methods
    // =============================================================================

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        appManager.changeActivityTitle(this, R.string.eventsAndActualities);

        actList = appManager.getActList();
        eventsList = appManager.getEventList();

        this.progressBar = (ProgressBar) findViewById(R.id.itemslist_progressBar);
//        this.progressBar.setVisibility(View.VISIBLE);

        this.initListView();
        this.fillListView();
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
        // this.listView.setVisibility(View.GONE);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MGEntry selectedEntry = (MGEntry) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                appManager.setSelectedEntry(selectedEntry);
                startActivity(intent);
                appManager.log("Activity launch", "INFO: Launching EntryActivity");
            }
        });
    }

    private void fillListView(){
        List<MGEntry> mixedList = (List<MGEntry>) actList;
        mixedList.addAll(eventsList);
        UniversalAdapter adapter = new UniversalAdapter(this,
                R.layout.list_item_row, mixedList);

        listView.setAdapter(adapter);
//        listView.setVisibility(View.INVISIBLE);
    }

    // =============================================================================
    // Subclasses
    // =============================================================================
}
