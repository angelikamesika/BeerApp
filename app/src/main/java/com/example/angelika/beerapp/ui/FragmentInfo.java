package com.example.angelika.beerapp.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.angelika.beerapp.R;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.RestaurantInfo;
import com.example.angelika.beerapp.providers.CityProvider;

import java.util.List;

/**
 * Created by Angelika on 02.08.2018.
 */

public class FragmentInfo extends Fragment {
    private EditText mSearch;
    private ListView mListViewCities;
    private CityAdapter mCityAdapter;
    private List<City> mListCities;


    private LinearLayout mLoCities;
    private LinearLayout mLoRestaurantInfo;
    private ListView mListRestaurantInfo;
    private TextView mTxtNoResults;
    private RelativeLayout mLoProgress;

    private RelativeLayout mLoSearchResult;
    private TextView mTvSearchResult;
    private ImageView mImgCloseSearchResult;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_info, container, false);
        mSearch = v.findViewById(R.id.search_city);
        mListViewCities = v.findViewById(R.id.list_cities);
        //   mCityAdapter = new CityAdapter(getContext(), mListCities);
        //   mListViewCities.setAdapter(mCityAdapter);
        mLoCities = v.findViewById(R.id.lo_cities);
        mLoRestaurantInfo = v.findViewById(R.id.lo_restaurant);
        mListRestaurantInfo = v.findViewById(R.id.lv_restaurant_info);
        mTxtNoResults = v.findViewById(R.id.txt_no_results);
        mLoProgress = v.findViewById(R.id.lo_progress);
        mLoProgress.setVisibility(View.GONE);

        mLoSearchResult = v.findViewById(R.id.lo_search_result);
        mTvSearchResult = v.findViewById(R.id.tv_search_result);
        mImgCloseSearchResult = v.findViewById(R.id.img_close_search_results);
        mImgCloseSearchResult.setImageDrawable(getActivity().getDrawable(R.drawable.ic_close_black_24dp));

        mImgCloseSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                hideSearchResult();
            }
        });


        // Add Text Change Listener to EditText
        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    showCitiesList();
                    mCityAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mListViewCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> aAdapterView, View aView, int aI, long aL) {
                String name = ((City) mCityAdapter.getItem(aI)).getCityName();
                showSearchResult(name);
                sendRequest(((City) mCityAdapter.getItem(aI)));
            }
        });

        hideSearchResult();
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
        return v;
    }

    private void hideSearchResult() {
        mLoSearchResult.setVisibility(View.GONE);
        mSearch.setVisibility(View.VISIBLE);
        mSearch.setText("");
        showCitiesList();
    }

    private void showSearchResult(String aCityName) {
        mLoSearchResult.setVisibility(View.VISIBLE);
        mSearch.setVisibility(View.GONE);
        mTvSearchResult.setText(aCityName);

    }

    public void showCitiesList() {
        mLoRestaurantInfo.setVisibility(View.GONE);
        mLoCities.setVisibility(View.VISIBLE);
        mListViewCities.setVisibility(View.VISIBLE);
        mTxtNoResults.setText("");
    }

    public void showRestaurantInfo(List<RestaurantInfo> list) {
        mLoRestaurantInfo.setVisibility(View.VISIBLE);
        mLoCities.setVisibility(View.GONE);
        RestaurantInfoAdapter adapter = new RestaurantInfoAdapter(getContext(), list);
        mListRestaurantInfo.setAdapter(adapter);
    }

    private void sendRequest(City aCity) {
        Activity activity = getActivity();
        if (activity instanceof MapsActivity) {
            ((MapsActivity) activity).sendRequest(aCity);
        }
    }

    public void showProgress() {
        mLoProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mLoProgress.setVisibility(View.GONE);
    }

    public void displayNotFoundLocation(City aCity) {
        mLoRestaurantInfo.setVisibility(View.GONE);
        mListViewCities.setVisibility(View.GONE);
        mLoCities.setVisibility(View.VISIBLE);
        mTxtNoResults.setText("No results for " + aCity.getCityName());
    }


    private class AsyncTaskRunner extends AsyncTask<String, List<City>, List<City>> {

        @Override
        protected List<City> doInBackground(String... params) {
            return new CityProvider().getCities();
        }

        @Override
        protected void onPostExecute(List<City> result) {
            // execution of result of Long time consuming operation
            mListCities = result;
            mCityAdapter = new CityAdapter(getContext(), mListCities);
            mListViewCities.setAdapter(mCityAdapter);
            mListViewCities.setVisibility(View.VISIBLE);
            hideProgress();
        }


        @Override
        protected void onPreExecute() {
            showProgress();
        }


    }

}


