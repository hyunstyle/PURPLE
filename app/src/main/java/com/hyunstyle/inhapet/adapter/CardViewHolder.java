package com.hyunstyle.inhapet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyunstyle.inhapet.R;

/**
 * Created by sh on 2018-04-10.
 */

public class CardViewHolder extends RecyclerView.ViewHolder {

    private ImageView cardImageView;
    private TextView cardTextView;
    private LinearLayout cardLayout;



    protected CardViewHolder(View v) {
        super(v);
        cardLayout = v.findViewById(R.id.menu_item_layout);
        cardImageView = v.findViewById(R.id.card_image);
        cardTextView = v.findViewById(R.id.card_text);



//        shareImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                        "://" + getResources().getResourcePackageName(cardImageView.getId())
//                        + '/' + "drawable" + '/' + getResources().getResourceEntryName((int) cardImageView.getTag()));
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
//                shareIntent.setType("image/jpeg");
//                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
//            }
//        });
    }

    protected LinearLayout getCardLayout() { return cardLayout; }

    protected ImageView getCardImageView() {
        return cardImageView;
    }

    protected TextView getCardTextView() {
        return cardTextView;
    }
}
