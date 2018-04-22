package com.hyunstyle.inhapet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunstyle.inhapet.R;

/**
 * Created by sh on 2018-04-20.
 */

public class ResultViewHolder extends RecyclerView.ViewHolder {

    private ImageView shopImageView;
    private TextView shopNameTextView;
    private TextView shopFamousMenuTextView;
    private TextView shopNumberOfLikesTextView;


    protected ResultViewHolder(View view) {
        super(view);

        shopImageView = view.findViewById(R.id.shop_image);
        shopNameTextView = view.findViewById(R.id.shop_name_text);
        shopFamousMenuTextView = view.findViewById(R.id.shop_famous_menu);
        shopNumberOfLikesTextView = view.findViewById(R.id.shop_number_of_like);
        //shopImageView = view.findViewById(R.layout.sho)

    }

    public ImageView getShopImageView() {
        return shopImageView;
    }

    public TextView getShopNameTextView() {
        return shopNameTextView;
    }

    public TextView getShopFamousMenuTextView() {
        return shopFamousMenuTextView;
    }

    public TextView getShopNumberOfLikesTextView() {
        return shopNumberOfLikesTextView;
    }
}
