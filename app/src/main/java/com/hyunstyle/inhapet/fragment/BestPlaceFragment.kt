package com.hyunstyle.inhapet.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.hyunstyle.inhapet.Config
import com.hyunstyle.inhapet.GridScrollLayoutManager
import com.hyunstyle.inhapet.R
import com.hyunstyle.inhapet.adapter.RestaurantAdapter
import com.hyunstyle.inhapet.adapter.ResultViewAdapter
import com.hyunstyle.inhapet.model.Restaurant

import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.find

/**
 * Created by SangHyeon on 2018-05-02.
 */

class BestPlaceFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var recyclerViewLayoutManager: RecyclerView.LayoutManager? = null
    private var realm: Realm? = null
    private lateinit var items: RealmResults<Restaurant>
    private var pageNumber: Int = 1
    private var itemCount: Int = 8
    private var resultAdapter: ResultViewAdapter? = null
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        progressBar.visibility = View.VISIBLE
        if (realm == null) {
            realm = Realm.getInstance(Config().get(context!!))
        }

        if(pageNumber == 1) {
            items = realm!!.where(Restaurant::class.java)
                    .between("id", 1, itemCount*pageNumber)
                    .findAllAsync()
        }


        //Log.e("size", "" + items.size)
        //resultAdapter.setData(realmResults)
        items.addChangeListener {
            result -> kotlin.run {
            Log.e("items", "" + result.size)
            resultAdapter!!.setData(result, 0)
            progressBar.visibility = View.GONE
        }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_best_place, container, false)

        init(view)
        return view
    }

    private fun init(view: View) {

        progressBar = view.findViewById(R.id.best_place_progress_bar)
        nestedScrollView = view.findViewById(R.id.best_place_nested_scroll_view)
        nestedScrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                //Log.e("count", "" + v!!.childCount)
                if(v!!.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                            scrollY > oldScrollY) {
                        // next scroll page loading
                        pageNumber += 1
                        loadMore()
                    }
                }
            }
        })

        recyclerView = view.findViewById(R.id.best_place_recycler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.isNestedScrollingEnabled = false
        recyclerViewLayoutManager = GridScrollLayoutManager(context, 2, GridScrollLayoutManager.VERTICAL, false)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.layoutManager = recyclerViewLayoutManager

        if(resultAdapter == null) {
            resultAdapter = ResultViewAdapter(context!!)
            resultAdapter!!.setHasStableIds(true)
        }

        recyclerView!!.adapter = resultAdapter


    }

    private fun loadMore() {

        progressBar.visibility = View.VISIBLE

        if(realm == null) {
            realm = Realm.getInstance(Config().get(context!!))
        }
        // 1 10   11 20   10n - 9 ,  10n
        items = realm!!.where(Restaurant::class.java)
                .between("id", 1, pageNumber * itemCount)
                .findAllAsync()

        //Log.e("size", "" + items.size)
        //resultAdapter.setData(realmResults)
        items.addChangeListener {
            result -> kotlin.run {
            resultAdapter!!.setData(result, 0)
            progressBar.visibility = View.GONE
        }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }


}// Required empty public constructor
