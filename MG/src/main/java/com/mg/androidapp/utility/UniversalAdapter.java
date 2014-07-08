package com.mg.androidapp.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mg.androidapp.R;
import com.mg.androidapp.common.AppManager;
import com.mg.objects.Actuality;
import com.mg.objects.Building;
import com.mg.objects.Exhibition;
import com.mg.objects.MGEntry;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.Date;
import java.util.List;

/**
* Adapter used to fill the list of buildings
*/
public class UniversalAdapter extends ArrayAdapter<MGEntry>{

    // =============================================================================
    // Fields
    // =============================================================================

    private Context context;
    int layoutResourceId;
    AppManager appManager = AppManager.getInstance();
    List<MGEntry> data;

    // =============================================================================
    // Constructors
    // =============================================================================

    public UniversalAdapter(Context context, int layoutResourceId, List<MGEntry> entries){
        super(context, layoutResourceId, entries);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = entries;
    }

    // =============================================================================
    // Overridden methods
    // =============================================================================
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final EntryHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            row.setBackgroundResource(R.drawable.gray_button);

            holder = new EntryHolder();
            holder.image = (ImageView)row.findViewById(R.id.list_item_image);
            holder.listItemText = (TextView)row.findViewById(R.id.list_item_text);
            holder.listItemDate = (TextView)row.findViewById(R.id.list_item_date);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.list_item_progressBar);

            row.setTag(holder);
        }
        else
        {
            holder = (EntryHolder)row.getTag();
        }

        // Adding data to listview components
        MGEntry entry = data.get(position);
        if (appManager.getAppLanguage().equals("cs")){
            holder.listItemText.setText(entry.getName());
        }
        else if (appManager.getAppLanguage().equals("en")){
            holder.listItemText.setText(entry.getEngName());
        }

        if(entry.getKind().equals("Constant Exhibition") ||
                entry.getKind().equals("Actual") ||
                entry.getKind().equals("Building")){
            holder.listItemDate.setVisibility(View.GONE);
        }
        else{
            holder.listItemDate.setVisibility(View.VISIBLE);
            Exhibition exhibition = (Exhibition) entry;
            Date date = exhibition.getDateFrom();;

            if (appManager.getDateString(exhibition.getDateFrom()).equals(
                    appManager.getDateString(exhibition.getDateTo()))){
                holder.listItemDate.setText(appManager.getDateString(date));
            }
            else {
                holder.listItemDate.setText(appManager.getDateString(date));

                date = exhibition.getDateTo();
                holder.listItemDate.setText(holder.listItemDate.getText() + " - "
                        + appManager.getDateString(date));
            }
        }

        //setting item color
        if (entry.getKind().equals("Building")){
            holder.listItemText.setTextColor(((Building) entry).getColor());
        }
        else{
            Actuality act = (Actuality) entry;
            if (!act.getBuildingIdList().isEmpty()){
                for (MGEntry bldng : appManager.getBuildingList()){
                    Building building = (Building) bldng;
                    if (act.getBuildingId(0).equals(building.getName())){
                        holder.listItemText.setTextColor(building.getColor());
                    }
                }
            }
        }

        // image downloading
        if (entry.getImageList().size() != 0){
            //holder.image.setVisibility(View.VISIBLE);
            final String actualityImageURI = appManager.createImageURI(entry.getImage(0).getId());

            appManager.log("Downloading Image","INFO: Downloading Image " + actualityImageURI);

            final ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(actualityImageURI, holder.image, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {}

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    Log.e("Image Loading ERROR", failReason.getType().name());
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    appManager.log("Downloading Image","INFO: Image " + actualityImageURI + " downloaded");
                    holder.progressBar.setVisibility(View.GONE);
                    holder.image.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    appManager.log("Downloading Image","INFO: Download of image " + actualityImageURI + " was cancelled");
                }
            });
        }
        else {
            holder.image.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        }

        return row;
    }


    // =============================================================================
    // Subclasses
    // =============================================================================

    static class EntryHolder
    {
        ImageView image;
        TextView listItemText;
        TextView listItemDate;
        ProgressBar progressBar;
    }
}
