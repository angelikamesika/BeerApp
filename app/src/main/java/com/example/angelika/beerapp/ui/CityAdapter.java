package com.example.angelika.beerapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.angelika.beerapp.R;
import com.example.angelika.beerapp.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 13.08.2018.
 */

public class CityAdapter extends BaseAdapter implements Filterable {

    private List<City> mOriginalValues; // Original Values
    private List<City> mDisplayedValues;    // Values to be displayed
    private LayoutInflater mInflater;
    private Context mContext;

    public CityAdapter(Context context, List<City> aList) {
        this.mOriginalValues = aList;
        this.mDisplayedValues = aList;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        //   return position;
        return mDisplayedValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout mLoContainer;
        TextView mTvName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_city_item, null);
            holder.mLoContainer = (LinearLayout) convertView.findViewById(R.id.list_item_lo);
            holder.mTvName = (TextView) convertView.findViewById(R.id.list_item_txt_city);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //  String txt = mDisplayedValues.get(position).getCityName() + "(" + mDisplayedValues.get(position).getCountryName() + ")";
        String txt = mDisplayedValues.get(position).getCityName();
        holder.mTvName.setText(txt);

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mDisplayedValues = (ArrayList<City>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<City> FilteredArrList = new ArrayList<City>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<City>(mDisplayedValues); // saves the original data in mOriginalValues
                }


//               If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//                  else does the Filtering and returns FilteredArrList(Filtered)
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).getCityName();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new City(mOriginalValues.get(i).getCityName(),
                                    mOriginalValues.get(i).getCountryName(), mOriginalValues.get(i).getLat(),
                                    mOriginalValues.get(i).getLng())
                            );
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}

