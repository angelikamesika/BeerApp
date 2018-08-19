package com.example.angelika.beerapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.angelika.beerapp.R;
import com.example.angelika.beerapp.model.RestaurantInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by Angelika on 16.08.2018.
 */

public class RestaurantInfoAdapter extends BaseAdapter {

    private List<RestaurantInfo> mRestaurantInfoList;
    private LayoutInflater mInflater;


    public RestaurantInfoAdapter(Context context, List<RestaurantInfo> aRestaurantInfoList) {
        mRestaurantInfoList = aRestaurantInfoList;
        Collections.sort(mRestaurantInfoList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mRestaurantInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRestaurantInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mTvInfo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RestaurantInfoAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new RestaurantInfoAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.list_restaurant_item, null);
            holder.mTvInfo = (TextView) convertView.findViewById(R.id.list_item_tv_info);
            convertView.setTag(holder);
        } else {
            holder = (RestaurantInfoAdapter.ViewHolder) convertView.getTag();
        }
        //  String txt = mDisplayedValues.get(position).getCityName() + "(" + mDisplayedValues.get(position).getCountryName() + ")";
        String txt = mRestaurantInfoList.get(position).getInfo();
        holder.mTvInfo.setText(txt);
        return convertView;
    }
}
