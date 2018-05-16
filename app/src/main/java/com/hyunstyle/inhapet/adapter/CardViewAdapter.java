package com.hyunstyle.inhapet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.dialog.SurveyDialog;
import com.hyunstyle.inhapet.model.Menu;

import java.util.ArrayList;

/**
 * Created by sh on 2018-04-10.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> implements View.OnClickListener{

    private ArrayList<Menu> menuList;
    private Context context;
    private SurveyDialog.OnSubmitListener onSubmitListener;

    public CardViewAdapter(ArrayList<Menu> menuList, Context context, SurveyDialog.OnSubmitListener onSubmitListener) {

        this.menuList = menuList;
        this.context = context;
        this.onSubmitListener = onSubmitListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_menu, parent, false);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        holder.getCardLayout().setTag(position);
        holder.getCardLayout().setOnClickListener(this);
        holder.getCardImageView().setImageResource(menuList.get(position).getImagePath());
        holder.getCardTextView().setText(menuList.get(position).getCardString());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        switch (view.getId()) {
            case R.id.menu_item_layout:
                SurveyDialog surveyDialog = new SurveyDialog(context, position, onSubmitListener, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                //surveyDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_appear_style;
                surveyDialog.show();
                // move to 설문
                Log.e("pos", "" + position);
                break;
        }
    }
}
