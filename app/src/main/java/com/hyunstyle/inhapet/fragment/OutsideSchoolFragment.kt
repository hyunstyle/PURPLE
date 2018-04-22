package com.hyunstyle.inhapet.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.bumptech.glide.Glide
import com.hyunstyle.inhapet.Config

import com.hyunstyle.inhapet.R
import com.hyunstyle.inhapet.adapter.CardViewAdapter
import com.hyunstyle.inhapet.adapter.RestaurantAdapter
import com.hyunstyle.inhapet.adapter.ResultViewAdapter
import com.hyunstyle.inhapet.adapter.TopAdViewPagerAdapter
import com.hyunstyle.inhapet.dialog.SurveyDialog
import com.hyunstyle.inhapet.interfaces.AsyncTaskResponse
import com.hyunstyle.inhapet.model.Restaurant
import com.hyunstyle.inhapet.thread.ImageUrlDownloadingThread
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONArray
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class OutsideSchoolFragment : Fragment(), AsyncTaskResponse, ViewPager.OnPageChangeListener, SurveyDialog.OnSubmitListener {
    override fun shrink() {
        //TODO 멀티스레드 shrink + recyclerview 생성 처리하기
        resultLayout.animate().translationY(-85f).withLayer()
        menuRecyclerView.startAnimation(shrinkAnim)
    }

    override fun filter(list: java.util.ArrayList<Int>, filterPosition: Int) {

        if(realm == null) {
            realm = Realm.getInstance(Config().get(context!!))
        }

        Log.e("filter", "" + filterPosition)
        when (filterPosition) {
            0 -> { // Restaurant Result

                Log.e("clickedLength~", "" + list.size)

                val realmResults = realm!!.where(Restaurant::class.java)
                        .`in`("subCategory", list.toTypedArray())
                        .findAllAsync()

                realmResults.addChangeListener {
                    result -> resultAdapter.setData(result)
                }
            }

            1 -> {

            }

            2 -> {
            }

            3 -> {
            }
        }
//        (((activity as MainActivity).adapter.getItem(1)) as ResultFragment).setTypeFilter(list, filterPosition)
//        (activity as MainActivity).viewPager.setCurrentItem(1, true)
    }

    private lateinit var appTitleView: ImageView
    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var resultLayout: RelativeLayout
    private lateinit var filterLayout: LinearLayout
    private lateinit var filterIcon: ImageView
    private lateinit var filterText: TextView
    private lateinit var shrinkAnim: Animation
    private lateinit var expandAnim: Animation

    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: ResultViewAdapter
    //private lateinit var resultAdapter: RestaurantAdapter
    private lateinit var resultRecyclerViewLayoutManager: RecyclerView.LayoutManager

    private lateinit var restaurantListView: ListView
    private lateinit var adapter: RestaurantAdapter
    private var realm: Realm? = null
    private lateinit var items: RealmResults<Restaurant>

    private lateinit var viewPager: ViewPager
    private lateinit var topAdViewPagerAdapter: TopAdViewPagerAdapter
    private lateinit var sliderDotLayout: LinearLayout
    private var dots: ArrayList<ImageView> = ArrayList()

    private val cardImages = arrayListOf(R.drawable.ic_outside_meal, R.drawable.ic_outside_drink, R.drawable.ic_outside_cafe, R.drawable.ic_outside_cafe)
    private val cardStrings = arrayListOf(R.string.menu_meal, R.string.menu_alcohol, R.string.menu_cafe, R.string.menu_snack)
    private val NUMBER_OF_CARD: Int = cardImages.size
    private var cardList: ArrayList<com.hyunstyle.inhapet.model.Menu> = ArrayList()
    private lateinit var menuRecyclerView: RecyclerView

    private lateinit var bestPlaceLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_outside_school, container, false)

        Log.e("outside oncreateview", "oncreate")
//        val cacheSize: Int = (Runtime.getRuntime().maxMemory() / 1024).toInt() / 8
//        Log.e("cacheSize", "" + cacheSize)

        //----------------------------------------------

        init(view)

        Glide.with(context!!)
                .load(ContextCompat.getDrawable(context!!, R.drawable.ic_banner))
//                .apply(RequestOptions.bitmapTransform(CropTransformation(Util.dip2px(context, 300f), Util.dip2px(context, 100f),
//                        CropTransformation.CropType.TOP)))
                .into(appTitleView)

        //val restaurantToggleButton = view.findViewById<Button>(R.id.toggle_restaurant)
        //restaurantToggleButton.setOnClickListener { v -> createRestaurantList() }

        return view
    }

    override fun onStart() {
        super.onStart()

        Log.e("onstart", "dd")

        if(urls.size == 0) {
            //val jsonArray: JSONArray? =
            ImageUrlDownloadingThread(this).execute(resources.getString(R.string.getImageURL), resources.getString(R.string.client))
        } else {
            topAdViewPagerAdapter = TopAdViewPagerAdapter(context!!, urls)
            viewPager.adapter = topAdViewPagerAdapter
            viewPager.addOnPageChangeListener(this)
            viewPager.setCurrentItem(0, true)
        }
    }

    private fun init(view: View) {
//        restaurantListView = view.findViewById(R.id.restaurant_list)
//        adapter = RestaurantAdapter(context!!, R.layout.list_item_restaurant)
//        restaurantListView.adapter = adapter
//        realm = Realm.getInstance(Config().get(context!!))

        appTitleView = view.findViewById(R.id.app_title)
        nestedScrollView = view.findViewById(R.id.outside_scroll_view)
        resultLayout = view.findViewById(R.id.result_layout)
        filterLayout = view.findViewById(R.id.filter_layout)
        filterLayout.isClickable = false
        filterLayout.setOnClickListener( {
            filterLayout.isClickable = false
            filterLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.content_background))
            filterIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_filter_14dp))
            filterText.setTextColor(ContextCompat.getColor(context!!, R.color.gray))
            resultLayout.animate().translationY(85f).withLayer()
            menuRecyclerView.startAnimation(expandAnim)
        })

        filterIcon = view.findViewById(R.id.filter_button)
        filterText = view.findViewById(R.id.filter_text)
        expandAnim = AnimationUtils.loadAnimation(context!!, R.anim.anim_expand)
        expandAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {
                menuRecyclerView.visibility = View.VISIBLE
            }
        })
        shrinkAnim = AnimationUtils.loadAnimation(context!!, R.anim.anim_shrink)
        shrinkAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                menuRecyclerView.visibility = View.GONE
                filterLayout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
                filterIcon.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_expand_16dp))
                filterText.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                filterLayout.isClickable = true
            }

            override fun onAnimationStart(p0: Animation?) {
                Log.e("start", "start")
            }

            override fun onAnimationRepeat(p0: Animation?) {
                Log.e("repeat", "repeat")
            }
        })

        resultRecyclerView = view.findViewById(R.id.result_list_view)
        resultRecyclerViewLayoutManager = GridLayoutManager(context!!, 2)
        resultRecyclerView.layoutManager = resultRecyclerViewLayoutManager
        resultAdapter = ResultViewAdapter()
        resultRecyclerView.adapter = resultAdapter
        //resultAdapter = RestaurantAdapter(context!!, R.layout.list_item_restaurant)
        //resultRecyclerView!!.adapter = resultAdapter

        viewPager = view.findViewById(R.id.image_viewpager)
        viewPager.setOnClickListener { v -> nestedScrollView.requestDisallowInterceptTouchEvent(true) }
        sliderDotLayout = view.findViewById(R.id.slider_dots_layout)
        menuRecyclerView = view.findViewById(R.id.menu_recycler_view)
        menuRecyclerView.setHasFixedSize(true)
        menuRecyclerView.setOnClickListener { v -> nestedScrollView.requestDisallowInterceptTouchEvent(true) }
        bestPlaceLayout = view.findViewById(R.id.best_place_container)

        for(i in 0 until NUMBER_OF_CARD) {
            val m = com.hyunstyle.inhapet.model.Menu()

            m.cardString = resources.getString(cardStrings[i])
            m.imagePath = cardImages[i]
            cardList.add(m)
        }

        val l = LinearLayoutManager(activity)
        l.orientation = LinearLayoutManager.HORIZONTAL
        menuRecyclerView.adapter = CardViewAdapter(cardList, context, this)
        menuRecyclerView.layoutManager = l


    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        for (i in 0 until dots.size) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_pager_selector_nonactivated_10dp))
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_pager_selector_activated_12dp))
    }


    override fun finished(output: JSONArray) {
        if(output != null) {
            if (output.length() > 0) {

                for (i in 0 until output.length()) {
                    val sb = StringBuilder(resources.getString(R.string.imagePrefix))
                    val u: String = output.get(i) as String

                    sb.append(u)

                    urls.add(sb.toString())

                    Log.e("url", sb.toString())
                }

                topAdViewPagerAdapter = TopAdViewPagerAdapter(context!!, urls)
                viewPager.adapter = topAdViewPagerAdapter

                for (i in 0 until topAdViewPagerAdapter.count) {
                    val img = ImageView(context!!)

                    if(i == 0)
                        img.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_pager_selector_activated_12dp))
                    else
                        img.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_pager_selector_nonactivated_10dp))

                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

                    params.setMargins(4, 0, 4, 0)

                    dots.add(img)
                    sliderDotLayout.addView(dots[i], params)
                }

                viewPager.addOnPageChangeListener(this)
                viewPager.setCurrentItem(0, true)

            } else {
                Log.d("no results", "no image.")
            }
        } else {
            Log.e("array", "null!")
        }
    }


    private fun createRestaurantList() {
        items = realm!!.where<Restaurant>(Restaurant::class.java).sort("id").findAllAsync() // 비동기로 찾기 종료시 addChangeListener 1회 실행
//        realm.where<Restaurant>(Restaurant::class.java).in("subCategory", dots)
        Log.e("size", "" + items.size)

        items.addChangeListener {
            result -> adapter.setData(result)
        }
    }

    companion object {
        var urls: ArrayList<String> = ArrayList()
    }

}
