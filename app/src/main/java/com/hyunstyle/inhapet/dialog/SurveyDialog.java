package com.hyunstyle.inhapet.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.adapter.SurveyAdapter;
import com.hyunstyle.inhapet.interfaces.SurveyResponse;
import com.hyunstyle.inhapet.util.WrapContentGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh on 2018-04-17.
 */

public class SurveyDialog extends Dialog implements SurveyResponse{

    private Context context;
    private ImageView logo;
    private int surveyType;
    private SurveyAdapter firstAdapter, secondAdapter, thirdAdapter;
    private WrapContentGridView firstTypeGridView;
    private WrapContentGridView secondTypeGridView;
    private WrapContentGridView thirdTypeGridView;
    private TextView headerTextView;
    private boolean[][] isChecked = new boolean[4][15];
    private OnSubmitListener onSubmitListener;
    private int filterPosition;

    public interface OnSubmitListener {
        void filter(ArrayList<List<Integer>> list, int filterPosition);
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
        firstTypeGridView = findViewById(R.id.filter_first_type_grid);
        secondTypeGridView = findViewById(R.id.filter_second_type_grid);
        thirdTypeGridView = findViewById(R.id.filter_third_type_grid);
        headerTextView = findViewById(R.id.filter_title_text);
        Button submit = findViewById(R.id.filter_submit);
        ImageButton cancel = findViewById(R.id.filter_close);
        submit.setOnClickListener(view -> surveySubmit(isChecked));
        cancel.setOnClickListener(view -> this.dismiss());

        switch (surveyType) {
            case 0: // Restaurant
                headerTextView.setText(context.getResources().getString(R.string.survey_restaurant));
                firstAdapter = new SurveyAdapter(context, 0, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_restaurant_menu), this);
                firstTypeGridView.setAdapter(firstAdapter);

                secondAdapter = new SurveyAdapter(context, 1, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_restaurant_bab_myeon_gogi_guk), this);
                secondTypeGridView.setAdapter(secondAdapter);

                thirdAdapter = new SurveyAdapter(context, 2, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_restaurant_solo_or_group), this);
                thirdTypeGridView.setAdapter(thirdAdapter);

                filterPosition = 0;
                break;
            case 1: // Alcohol
                headerTextView.setText(context.getResources().getString(R.string.survey_alcohol));
                firstAdapter = new SurveyAdapter(context, 0, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_alcohol_type), this);
                firstTypeGridView.setAdapter(firstAdapter);

                secondAdapter = new SurveyAdapter(context, 1, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_alcohol_food), this);
                secondTypeGridView.setAdapter(secondAdapter);

                thirdAdapter = new SurveyAdapter(context, 2, R.layout.list_item_restaurant_type,
                        context.getResources().getStringArray(R.array.category_alcohol_mood), this);
                thirdTypeGridView.setAdapter(thirdAdapter);

                filterPosition = 1;

                break;
            case 2: // Cafe
                break;
            case 3: // Snack
                break;
        }
    }

    private void surveySubmit(boolean[][] checkedList) {
        ArrayList<List<Integer>> checkedNumber = new ArrayList<>(checkedList.length);

        for(int i = 0; i<checkedList.length; i++) {
            List<Integer> typeCheckedNumber = new ArrayList<>();
            for(int j = 0; j<checkedList[i].length; j++) {
                if(checkedList[i][j]) {
                    typeCheckedNumber.add(j+1);
                }
            }

            Log.e("list length", "" + checkedList[i].length);

            if(typeCheckedNumber.size() == 0) { // 선택안했으면 전부 선택한 것으로 취급
                for(int j = 0; j<checkedList[i].length; j++) {
                    typeCheckedNumber.add(j+1);
                }
            }
            checkedNumber.add(typeCheckedNumber);
        }

//        for(int i = 0; i<checkedList.length; i++) {
//            if(checkedList[i]) {
//                checkedNumber.add(i+1);
//            }
//        }

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
    public void clicked(int menuType, int position, @NonNull View view, @NonNull TextView textView) {

        if(isChecked[menuType][position]) {
            isChecked[menuType][position] = false;
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_round));
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            isChecked[menuType][position] = true;
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_round_activated));
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }
}
