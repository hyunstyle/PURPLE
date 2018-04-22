package com.hyunstyle.inhapet.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunstyle.inhapet.R;

//import net.daum.mf.map.api.MapView;

import javax.annotation.Nonnull;

/**
 * Created by sh on 2018-04-04.
 */

public class ShopInfoFragment extends Fragment {

    private Context context;

    public ShopInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

//        MapView mapView = new MapView(context);
//
//        ViewGroup viewLayout = view.findViewById(R.id.map_view);
//        viewLayout.addView(mapView);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
