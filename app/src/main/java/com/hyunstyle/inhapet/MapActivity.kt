package com.hyunstyle.inhapet

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.hyunstyle.inhapet.navermap.NMapPOIflagType
import com.hyunstyle.inhapet.navermap.NMapViewerResourceProvider
import com.nhn.android.maps.NMapActivity
import com.nhn.android.maps.NMapController
import com.nhn.android.maps.NMapLocationManager
import com.nhn.android.maps.NMapView
import com.nhn.android.maps.maplib.NGeoPoint
import com.nhn.android.maps.nmapmodel.NMapError
import com.nhn.android.maps.overlay.NMapPOIdata
import com.nhn.android.maps.overlay.NMapPOIitem
import com.nhn.android.mapviewer.overlay.NMapOverlayManager
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay

/**
 * Created by SangHyeon on 2018-05-02.
 */

class MapActivity : NMapActivity(), NMapView.OnMapStateChangeListener, NMapPOIdataOverlay.OnStateChangeListener, NMapLocationManager.OnLocationChangeListener {

    private lateinit var mapView: NMapView
    private var nMapController: NMapController? = null
    private var nMapViewerResourceProvider: NMapViewerResourceProvider? = null
    private var nMapOverlayManager: NMapOverlayManager? = null
    private var nMapLocationManager: NMapLocationManager? = null
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_map)

        init()
        initMapView()
        moveMapCenter()
    }

    private fun init() {
        longitude = intent.getDoubleExtra(resources.getString(R.string.map_longitude), 0.0)
        latitude = intent.getDoubleExtra(resources.getString(R.string.map_latitude), 0.0)

        val backButton = findViewById<ImageButton>(R.id.map_back_button)
        backButton.setOnClickListener { click -> onBackPressed() }
        val shopName = intent.getStringExtra(resources.getString(R.string.map_shop_name))
        val shopNameTextView = findViewById<TextView>(R.id.map_shop_name)
        shopNameTextView.text = shopName

        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun initMapView() {

        mapView = findViewById(R.id.map_view)
        mapView.setClientId(resources.getString(R.string.api_key))
        mapView.isClickable = true
        mapView.setScalingFactor(2.0f, false)

        mapView.setOnMapStateChangeListener(this)

        nMapController = mapView.mapController
        nMapViewerResourceProvider = NMapViewerResourceProvider(this)
        nMapOverlayManager = NMapOverlayManager(this, mapView, nMapViewerResourceProvider)

        nMapLocationManager = NMapLocationManager(this)
        nMapLocationManager!!.setOnLocationChangeListener(this)
        //nMapLocationManager!!.enableMyLocation(false)
    }

    private fun moveMapCenter() {
        //Log.e("move", "maxZoomLev : " + nMapController!!.getMaxZoomLevel() + "cur : " + nMapController.getZoomLevel())
        val currentPoint = NGeoPoint(longitude, latitude)
        nMapController!!.zoomLevel = nMapController!!.maxZoomLevel
        nMapController!!.mapCenter = currentPoint

        // poi item의 개수 첫번째인자
        Log.e("move", "" + longitude + ", " + latitude)
        val poiData = NMapPOIdata(1, nMapViewerResourceProvider)
        poiData.addPOIitem(longitude, latitude, "현재 위치", NMapPOIflagType.PIN, 0)
        //poiData.addPOIitem(myLongitude + 0.001, myLatitude + 0.002, "도착 위치", NMapPOIflagType.PIN, 0)
        poiData.endPOIdata()

        val poiDataOverlay = nMapOverlayManager!!.createPOIdataOverlay(poiData, null)
        //poiDataOverlay.showAllPOIdata(0)
        poiDataOverlay.onStateChangeListener = this
    }












    override fun onMapCenterChangeFine(p0: NMapView?) {

    }

    override fun onAnimationStateChange(p0: NMapView?, p1: Int, p2: Int) {

    }

    override fun onMapInitHandler(p0: NMapView?, p1: NMapError?) {

    }

    override fun onZoomLevelChange(p0: NMapView?, p1: Int) {

    }

    override fun onMapCenterChange(p0: NMapView?, p1: NGeoPoint?) {

    }

    override fun onFocusChanged(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {

    }

    override fun onCalloutClick(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {

    }

    override fun onLocationChanged(p0: NMapLocationManager?, p1: NGeoPoint?): Boolean {
        return true
    }

    override fun onLocationUpdateTimeout(p0: NMapLocationManager?) {

    }

    override fun onLocationUnavailableArea(p0: NMapLocationManager?, p1: NGeoPoint?) {

    }


}