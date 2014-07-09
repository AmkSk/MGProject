package com.mg.androidapp.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Gallery View used in EntryActivity and BuildingActivity
 */
public class GalleryAdapter extends PagerAdapter {
    Context context;
    List<Drawable> drawableList = new ArrayList<Drawable>();

    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return drawableList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawableList.get(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    public void addDrawable(Drawable drawable) {
        this.drawableList.add(drawable);
        this.notifyDataSetChanged();
    }
}

