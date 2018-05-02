package com.hyunstyle.inhapet.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsideSchoolFragment extends Fragment {

    public InsideSchoolFragment() {

        List<Restaurant> list = new ArrayList<>();

        List<String> nameList = new ArrayList<>();
        for(Restaurant item : list) {
            nameList.add(item.getName());
        }
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("inside oncreateview", "oncreate");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inside_school, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
