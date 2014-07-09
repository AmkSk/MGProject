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
import com.mg.objects.Exhibition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calendar of all exhibitions and events
 * The list uses its own adapter - CalendarAdapter
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

    private void initListView(){
        this.listView = (ListView)findViewById(R.id.calendar_ListView);
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

        CalendarAdapter adapter = new CalendarAdapter(this,
                R.layout.calendar_item_row, itemList);
        this.listView.setAdapter(adapter);
    }

    // =============================================================================
    // Subclasses
    // =============================================================================
}
