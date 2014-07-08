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

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

//        holder.itemHeader.setText(data.get(position).getName());

        fillRowWithData(holder,position);
        return row;
    }


    // =============================================================================
    // Methods
    // =============================================================================

    private void fillRowWithData(ItemHolder holder, int position){

        Exhibition item = data.get(position);

        holder.itemHeader.setText(item.getName());
        holder.itemDate.setText(appManager.getDateString(item.getDateFrom())
        + " - " +data.get(position).getDateTo());
        holder.dateHeader.setText(appManager.getDateStringShort(item.getDateFrom()));

        holder.itemHeader.setTypeface(appManager.getFontBold());
    }

    // =============================================================================
    // Subclasses
    // =============================================================================

    // =============================================================================
    // Subclasses
    // =============================================================================

    static class ItemHolder
    {
        TextView dateHeader;
        TextView itemHeader;
        TextView itemDate;
    }
}
