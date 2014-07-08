package com.mg.androidapp.utility;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;

import java.util.Calendar;
import java.util.List;

/**
 * Created by amk on 8.7.2014.
 */
public class CalendarAdapter extends ArrayAdapter<Exhibition> {
    // =============================================================================
    // Fields
    // =============================================================================

    private Context context;
    int layoutResourceId;
    AppManager appManager = AppManager.getInstance();
    List<Exhibition> data;
    List<Building> buildingList;

    // =============================================================================
    // Constructors
    // =============================================================================

    public CalendarAdapter(Context context, int resource, List<Exhibition> objects) {
        super(context, resource, objects);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = objects;
        this.buildingList = (List<Building>) appManager.getBuildingList();
    }

    // =============================================================================
    // Overriden methods
    // =============================================================================

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ItemHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            row.setBackgroundResource(R.drawable.gray_button);

            holder = new ItemHolder();
            holder.dateHeader = (TextView)row.findViewById(R.id.calendar_dateHeaderText);
            holder.itemHeader = (TextView)row.findViewById(R.id.calendar_itemText);
            holder.itemDate = (TextView)row.findViewById(R.id.calendar_itemDateText);
            holder.separator = (View)row.findViewById(R.id.calendar_separator);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        holder.separator.setVisibility(View.VISIBLE);
        holder.dateHeader.setVisibility(View.VISIBLE);

        Exhibition item = data.get(position);

        fillRowWithData(position, holder, item);
        updateCalendarItems(position, item, holder);
        addColor(item, holder);

        return row;
    }


    // =============================================================================
    // Methods
    // =============================================================================

    private void fillRowWithData(int position,ItemHolder holder, Exhibition item){

        if (appManager.getAppLanguage().equals("cs")){
            holder.itemHeader.setText(item.getName());
        }
        else {
            holder.itemHeader.setText(item.getEngName());
        }

        holder.itemDate.setText(appManager.getDateString(item.getDateFrom())
        + " - " + appManager.getDateString(data.get(position).getDateTo()));
        holder.dateHeader.setText(appManager.getDateStringShort(item.getDateFrom()));
        holder.itemHeader.setTypeface(appManager.getFontBold());
    }

    private void updateCalendarItems(int position, Exhibition item, ItemHolder holder){
        if (position == data.size()-1 || position == 0)
            return;

        Exhibition itemAfter = data.get(position+1);
        Exhibition itemBefore = data.get(position-1);

        String dateAfter = appManager.getDateString(itemAfter.getDateFrom());
        String date = appManager.getDateString(item.getDateFrom());
        String dateBefore = appManager.getDateString(itemBefore.getDateFrom());

        if(dateAfter.equals(date)) {
            holder.separator.setVisibility(View.INVISIBLE);
        }

        if(dateBefore.equals(date)){
            holder.dateHeader.setVisibility(View.INVISIBLE);
        }
    }

    private void addColor(Exhibition item, ItemHolder holder){

        if (!item.getBuildingIdList().isEmpty()){
            for (MGEntry bldng : appManager.getBuildingList()){
                Building building = (Building) bldng;
                if (item.getBuildingId(0).equals(building.getName())){
                    holder.dateHeader.setTextColor(building.getColor());
                    holder.itemHeader.setTextColor(building.getColor());
                    holder.itemDate.setTextColor(building.getColor());
                }
            }
        }
    }

    // =============================================================================
    // Subclasses
    // =============================================================================

    static class ItemHolder
    {
        TextView dateHeader;
        TextView itemHeader;
        TextView itemDate;
        View separator;
    }
}
