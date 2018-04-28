package com.hyunstyle.inhapet.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyunstyle.inhapet.R;

/**
 * Created by sh on 2018-04-20.
 */

public class ResultViewHolder extends RecyclerView.ViewHolder {

    private ImageView shopImageView;
    private TextView shopNameTextView;
    private TextView shopFamousMenuTextView;
    private TextView shopNumberOfLikesTextView;
    private RelativeLayout layout;


    protected ResultViewHolder(View view) {
        super(view);

        layout = view.findViewById(R.id.result_container);
        shopImageView = view.findViewById(R.id.shop_image);
        Glide.with(view.getContext())
                .load(ContextCompat.getDrawable(view.getContext(), R.drawable.test))
//                .apply(RequestOptions.bitmapTransform(CropTransformation(Util.dip2px(context, 300f), Util.dip2px(context, 100f),
//                        CropTransformation.CropType.TOP)))
                .into(shopImageView);

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

    public RelativeLayout getLayout() {
        return layout;
    }

    public TextView getShopFamousMenuTextView() {
        return shopFamousMenuTextView;
    }

    public TextView getShopNumberOfLikesTextView() {
        return shopNumberOfLikesTextView;
    }
}
