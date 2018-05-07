package com.hyunstyle.inhapet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.hyunstyle.inhapet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SangHyeon on 2018-05-02.
 */

 /*
    Referred from http://www.zoftino.com/android-autocompletetextview-custom-layout-and-adapter
 */
public class AutoCompleteListAdapter extends ArrayAdapter {

    private List<SpannableStringBuilder> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;

    public AutoCompleteListAdapter(Context context, int resource, List<SpannableStringBuilder> dataList) {
        super(context, resource, dataList);
        this.dataList = dataList;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public SpannableStringBuilder getItem(int position) {

        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = view.findViewById(R.id.item_restaurant_name);
        strName.setText(getItem(position), TextView.BufferType.SPANNABLE);
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<String>();
                    for(SpannableStringBuilder data : dataList) {
                        dataListAllItems.add(data.toString());
                    }
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = null;
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();
                ArrayList<SpannableStringBuilder> buildersList = new ArrayList<>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        SpannableStringBuilder sp = new SpannableStringBuilder(dataItem);
                        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                                dataItem.toLowerCase().indexOf(searchStrLowerCase), (dataItem.toLowerCase().indexOf(searchStrLowerCase) + searchStrLowerCase.length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        //dataItem.indexOf(searchStrLowerCase)
                        matchValues.add(dataItem);
                        buildersList.add(sp);
                    }
                }

                for(String dataItem : dataListAllItems) {
                    if(!matchValues.contains(dataItem) && dataItem.toLowerCase().contains(searchStrLowerCase)) {
                        SpannableStringBuilder sp = new SpannableStringBuilder(dataItem);
                        sp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                                dataItem.toLowerCase().indexOf(searchStrLowerCase), (dataItem.toLowerCase().indexOf(searchStrLowerCase) + searchStrLowerCase.length()), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            //dataItem.indexOf(searchStrLowerCase)
                        matchValues.add(dataItem);
                        buildersList.add(sp);
                    }
                }


                results.values = buildersList;
                results.count = buildersList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<SpannableStringBuilder>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
