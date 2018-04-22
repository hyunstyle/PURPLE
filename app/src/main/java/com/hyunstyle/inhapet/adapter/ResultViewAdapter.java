package com.hyunstyle.inhapet.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.model.Restaurant;

import java.util.Collections;
import java.util.List;

/**
 * Created by sh on 2018-04-20.
 */

public class ResultViewAdapter extends RecyclerView.Adapter<ResultViewHolder> {

    private List<Restaurant> resultList = Collections.emptyList();

    public ResultViewAdapter() {

    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_result, parent, false);

        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        //TODO : shop 이미지, 좋아요 없음
        //holder.getShopImageView().setImageResource(resultList.get(position).get);

        holder.getShopNameTextView().setText(resultList.get(position).getName());
        holder.getShopFamousMenuTextView().setText(resultList.get(position).getFamousMenu());
    }

    public void setData(List<Restaurant> data) {

        this.resultList = data;
        notifyDataSetChanged();
//        notifyItemRangeInserted(prevSize, size);
//        notifyItemRangeChanged(prevSize, size);
    }

//    public void addData(List<Restaurant> data) {
//
//        for(Restaurant r: data) {
//            this.resultList.add(r);
//        }
//        //resultList.addAll(data);
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
