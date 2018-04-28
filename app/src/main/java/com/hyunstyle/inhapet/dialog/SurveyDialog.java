package com.hyunstyle.inhapet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.adapter.SurveyAdapter;
import com.hyunstyle.inhapet.interfaces.SurveyResponse;
import com.hyunstyle.inhapet.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by sh on 2018-04-17.
 */

public class SurveyDialog extends Dialog implements SurveyResponse{

    private Context context;
    private ImageView logo;
    private int surveyType;
    private SurveyAdapter adapter;
    private GridView typeView;
    private boolean[] isChecked = new boolean[10];
    private OnSubmitListener onSubmitListener;
    private int filterPosition;

    public interface OnSubmitListener {
        void filter(ArrayList<Integer> list, int filterPosition);
        void shrink();
    }

    public SurveyDialog(Context context, int type, OnSubmitListener onSubmitListener) {
        super(context);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //setCanceledOnTouchOutside(false);

        this.context = context;
        this.surveyType = type;
        this.onSubmitListener = onSubmitListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_survey_dialog);
        getWindow().getAttributes().windowAnimations = R.style.dialog_appear_style;
        typeView = findViewById(R.id.type_grid);
        Button submit = findViewById(R.id.survey_submit);
        Button cancel = findViewById(R.id.survey_cancel);

        submit.setOnClickListener(view -> surveySubmit(isChecked));
        cancel.setOnClickListener(view -> this.dismiss());

        switch (surveyType) {
            case 0: // Restaurant
                adapter = new SurveyAdapter(context, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_list), this);
                typeView.setAdapter(adapter);
                filterPosition = 0;
                break;
            case 1: // Alcohol
                break;
            case 2: // Cafe
                break;
            case 3: // Snack
                break;
        }
    }

    private void surveySubmit(boolean[] checkedList) {
        ArrayList<Integer> checkedNumber = new ArrayList<>();
        for(int i = 0; i<checkedList.length; i++) {
            if(checkedList[i]) {
                checkedNumber.add(i+1);
            }
        }


        //onSubmitListener.filter(checkedNumber, filterPosition);

        //onSubmitListener.shrink();
        onSubmitListener.filter(checkedNumber, filterPosition);
        this.dismiss();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                onSubmitListener.filter(checkedNumber, filterPosition);
//            }
//        }, 600);


        //this.dismiss();
        //onSubmitListener.shrink();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void clicked(int position, @NonNull View view, @NonNull TextView textView) {

        Log.e("clicked at", "" + position);
        if(isChecked[position]) {
            isChecked[position] = false;
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_round));
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            isChecked[position] = true;
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_round_activated));
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }
}
