package com.hyunstyle.inhapet.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyunstyle.inhapet.R;
import com.hyunstyle.inhapet.navermap.NMapFragment;
import com.hyunstyle.inhapet.navermap.NMapPOIflagType;
import com.hyunstyle.inhapet.navermap.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

/**
 * Created by SangHyeon on 2018-03-10.
 */

public class MapViewFragment extends NMapFragment implements NMapView.OnMapStateChangeListener, NMapPOIdataOverlay.OnStateChangeListener, NMapLocationManager.OnLocationChangeListener{

    // TODO : nested scrollview 지도 드래그 개별 인식하도록

    private Context context;
    private OnTouchListener mListener;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 300;

    private NMapView nMapView;
    private NMapController nMapController;
    private NMapViewerResourceProvider nMapViewerResourceProvider;
    private NMapOverlayManager nMapOverlayManager;

    private NMapLocationManager nMapLocationManager;

    private double myLongitude; // 경도
    private double myLatitude; // 위도


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        nMapView = (NMapView) v.findViewById(R.id.map_view);
        nMapView.setClientId(getResources().getString(R.string.api_key));
        nMapView.setClickable(true);

        ImageView i = new ImageView(context);

        // nested scrollview 스크롤과 구별하기 위해서 Wrapper 클래스 사용
        TouchableWrapper frameLayout = new TouchableWrapper(context);
        frameLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        ((ViewGroup) v).addView(frameLayout,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return v;
    }

    public void setListener(OnTouchListener listener) {
        mListener = listener;
    }

    public interface OnTouchListener {
        void onTouch();
    }

    protected class TouchableWrapper extends FrameLayout {

        public TouchableWrapper(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mListener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    mListener.onTouch();
                    break;
            }
            return super.dispatchTouchEvent(event);
        }
    }


    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("here", "intentif");
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Permission");
            builder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.setNegativeButton(android.R.string.no, null);
            builder.show();
        } else {
            Log.e("here", "intentelse");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "위치 허용", Toast.LENGTH_SHORT).show();
                } else {

                    Log.e("here", "intent");
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        checkPermission();

        nMapView.setBuiltInZoomControls(true, null);
        nMapView.setOnMapStateChangeListener(this);

        nMapController = nMapView.getMapController();
        nMapViewerResourceProvider = new NMapViewerResourceProvider(context);
        nMapOverlayManager = new NMapOverlayManager(context, nMapView, nMapViewerResourceProvider);

        nMapLocationManager = new NMapLocationManager(context);
        nMapLocationManager.setOnLocationChangeListener(this);
        nMapLocationManager.enableMyLocation(false);

        myLatitude = 37.4522408;//= nGeoPoint.getLatitude();
        myLongitude = 126.6569483;//= nGeoPoint.getLongitude();

        moveMapCenter();
    }

    private void moveMapCenter() {
        Log.e("move", "maxZoomLev : " + nMapController.getMaxZoomLevel() + "cur : " + nMapController.getZoomLevel());
        NGeoPoint currentPoint = new NGeoPoint(myLongitude, myLatitude);
        nMapController.setZoomLevel(nMapController.getMaxZoomLevel());
        nMapController.setMapCenter(currentPoint);

        // poi item의 개수 첫번째인자
        Log.e("move", myLongitude + ", " + myLatitude);
        NMapPOIdata poiData = new NMapPOIdata(2, nMapViewerResourceProvider);
        poiData.addPOIitem(myLongitude, myLatitude, "현재 위치", NMapPOIflagType.PIN, 0);
        poiData.addPOIitem(myLongitude + 0.001, myLatitude + 0.002, "도착 위치", NMapPOIflagType.PIN, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = nMapOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(this);

    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) {
            //moveMapCenter();
        } else {
            Log.e("map init error", nMapError.message);
        }

    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {
        Log.e("zoomlev", "" + i);
    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }

    @Override
    public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }


    ////////// 내 위치 콜백 인터페이스

    @Override
    public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

        Log.e("locationch", "changed " + nGeoPoint.getLatitude() + " " + nGeoPoint.getLongitude());
        //myLatitude = 126.6569483;//= nGeoPoint.getLatitude();
        //myLongitude = 37.4522408;//= nGeoPoint.getLongitude();
        return true;
    }

    @Override
    public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
        Log.d("location", "timeout");
    }

    @Override
    public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
        Log.d("location", "out of area");
    }
}
