package com.mg.androidapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.androidapp.utility.CalendarAdapter;
import com.mg.androidapp.utility.UniversalAdapter;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by amk on 8.7.2014.
 */
public class CalendarActivity extends Activity{
    // =============================================================================
    // Fields
    // =============================================================================

    AppManager appManager;
    private List<Exhibition> itemList;
    ListView listView;

    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        appManager = AppManager.getInstance();
        appManager.initNavigation(this);

        appManager.changeActivityTitle(this, R.string.calendar);

        itemList = new ArrayList<Exhibition>();
        listView = (ListView) findViewById(R.id.calendar_ListView);

        itemList.addAll((List<Exhibition>)appManager.getEventList());
        itemList.addAll((List<Exhibition>)appManager.getExhibitionsList());

        Collections.sort(itemList);
        this.initListView();
    }

    // =============================================================================
    // Methods
    // =============================================================================

    private void sortTest (){
        for (Exhibition item : itemList){
            appManager.log("test", "INFO: " + item.getDateFrom().getTime());
        }
    }

    private void initListView(){
        this.listView = (ListView)findViewById(R.id.calendar_ListView);
//        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Building selectedBuilding = (Building) adapterView.getItemAtPosition(i);
//                Intent intent = new Intent(getApplicationContext(), BuildingActivity.class);
//                appManager.setSelectedBuilding(selectedBuilding);
//                startActivity(intent);
//                appManager.log("Activity launch", "INFO: Launching ActualActivity");
//            }
//        });

        CalendarAdapter adapter = new CalendarAdapter(this,
                R.layout.calendar_item_row, itemList);
        this.listView.setAdapter(adapter);
    }

    // =============================================================================
    // Subclasses
    // =============================================================================
}
