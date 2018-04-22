package com.hyunstyle.inhapet.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView

import com.hyunstyle.inhapet.Config
import com.hyunstyle.inhapet.R
import com.hyunstyle.inhapet.adapter.RestaurantAdapter
import com.hyunstyle.inhapet.model.Restaurant

import java.util.ArrayList

import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

class ResultFragment : Fragment() {

    private var nestedScrollView: NestedScrollView? = null
    private var checkedFilterList: ArrayList<Int>? = null
    private var realm: Realm? = null
    private var resultListView: ListView? = null
    private var resultAdapter: RestaurantAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_result, container, false)

        init(v)

        val showMapButton = v.findViewById<Button>(R.id.showMap)
        showMapButton.setOnClickListener { view -> showMap() }

        //Button test = v.findViewById(R.id.)

        //nestedScrollView = v.findViewById(R.id.parent_scrollView)
        //nestedScrollView.setNestedScrollingEnabled(false);

        //nestedScrollView.requestChildFocus();

        return v
    }

    private fun init(view: View) {
        resultListView = view.findViewById(R.id.restaurant_list)
        resultAdapter = RestaurantAdapter(context!!, R.layout.list_item_restaurant) //RestaurantAdapter(context!!, R.layout.list_item_restaurant)
        resultListView!!.adapter = resultAdapter
    }


    fun setTypeFilter(checkedList: ArrayList<Int>, which: Int) {

        if(realm == null) {
            realm = Realm.getInstance(Config().get(context!!))
        }

        Log.e("filter", "" + which)
        when (which) {
            0 -> { // Restaurant Result

                Log.e("clickedLength~", "" + checkedList.size)

                val realmResults = realm!!.where(Restaurant::class.java)
                        .`in`("subCategory", checkedList.toTypedArray())
                        .findAllAsync()

                realmResults.addChangeListener {
                    result -> resultAdapter!!.setData(result)
                }
            }

            1 -> {

            }

            2 -> {
            }

            3 -> {
            }
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun showMap() {

        //ShopInfoFragment shopInfoFragment = new ShopInfoFragment();

        val childFragment = MapViewFragment()

        childFragment.setListener { nestedScrollView!!.requestDisallowInterceptTouchEvent(true) }

        setChildFragment(childFragment)
    }

    private fun setChildFragment(child: Fragment) {
        val childFragmentTransaction = childFragmentManager.beginTransaction()

        childFragmentTransaction.addToBackStack(null)
        childFragmentTransaction.add(R.id.map_container, child, "crft")
        childFragmentTransaction.commit()
    }
}// Required empty public constructor
