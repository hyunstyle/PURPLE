package com.hyunstyle.inhapet

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.hyunstyle.inhapet.model.Restaurant
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

import io.realm.Realm
import kotlinx.android.synthetic.main.content_shop_info.*

/**
 * Created by sh on 2018-04-28.
 */

class ShopInfoActivity : NMapActivity(), NMapView.OnMapStateChangeListener, NMapPOIdataOverlay.OnStateChangeListener, NMapLocationManager.OnLocationChangeListener {


    private var realm: Realm? = null
    private var restaurant: Restaurant? = null
    private lateinit var shopName: TextView
    private lateinit var shopPhoneNumber: TextView
    private lateinit var shopAddress: TextView
    private lateinit var shopBusinessHours: TextView
    private lateinit var shopAveragePrices: TextView

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private lateinit var mapView: NMapView
    private var nMapController: NMapController? = null
    private var nMapViewerResourceProvider: NMapViewerResourceProvider? = null
    private var nMapOverlayManager: NMapOverlayManager? = null
    private var nMapLocationManager: NMapLocationManager? = null

    //private var mListener: OnTouchListener? = null

    private var nestedScrollView: NestedScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_shop_info)

        init()

        //TODO Touchable wrapper not working
        val wrapper = TouchableWrapper(this)
        wrapper.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))

        val rootView = window.decorView.rootView as ViewGroup
        rootView.addView(wrapper, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        if (realm == null) {
            realm = Realm.getInstance(Config().get(this))

            restaurant = realm!!.where(Restaurant::class.java)
                    .equalTo("id", intent.extras!!.getInt("pk"))
                    .findFirst()

            if(restaurant == null) {
                Toast.makeText(this, resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
            } else {
                initInfo()
            }
            //realm.where()

            //realm.where()
        }
    }

    private fun init() {
        var closeButton = findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener { view -> onBackPressed() }
    }

    private fun initInfo() {

        shopName = findViewById(R.id.shop_name)
        shopPhoneNumber = findViewById(R.id.shop_calling_number)
        shopAddress = findViewById(R.id.shop_address)
        shopBusinessHours = findViewById(R.id.shop_business_hours)
        shopAveragePrices = findViewById(R.id.shop_average_prices)

        shopName.text = restaurant!!.name
        shopPhoneNumber.text = restaurant!!.phone
        shopAddress.text = restaurant!!.location
        shopBusinessHours.text = restaurant!!.businessHours
        longitude = restaurant!!.longitude
        latitude = restaurant!!.latitude

        nestedScrollView = findViewById(R.id.shop_scroll_container)

        //setListener(scrollContainer.requestDisallowInterceptTouchEvent(true))

        //nestedScrollView!!.requestDisallowInterceptTouchEvent(true)

        initMapView()
        moveMapCenter()
    }

    private fun initMapView() {

        mapView = findViewById(R.id.map_view)
        mapView.setClientId(resources.getString(R.string.api_key))
        mapView.isClickable = true

        mapView.setBuiltInZoomControls(true, null)
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
        poiDataOverlay.showAllPOIdata(0)
        poiDataOverlay.onStateChangeListener = this
    }

    private inner class TouchableWrapper(context: Context) : FrameLayout(context) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouch()
                MotionEvent.ACTION_UP -> onTouch()
            }
            return super.dispatchTouchEvent(event)
        }
    }

//    private fun setListener(listener: OnTouchListener) {
//        mListener = listener
//    }
//
//    interface OnTouchListener {
//        fun onTouch()
//    }

    private fun onTouch() {
        nestedScrollView!!.requestDisallowInterceptTouchEvent(true)
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
