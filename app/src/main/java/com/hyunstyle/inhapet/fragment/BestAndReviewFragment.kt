package com.hyunstyle.inhapet.fragment

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hyunstyle.inhapet.R

/**
 * Created by SangHyeon on 2018-05-01.
 */
class BestAndReviewFragment : Fragment() {


    private var viewPager: ViewPager? = null
    private var viewPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_best_and_review, container, false)

        init(v)

        return v
    }

    private fun init(view: View) {
        viewPager = view.findViewById(R.id.best_and_review_view_pager)
        if(viewPagerAdapter == null) {
            viewPagerAdapter = SectionsPagerAdapter(childFragmentManager)
            viewPager!!.adapter = viewPagerAdapter
        } else {
            viewPager!!.adapter = viewPagerAdapter
        }

        val tabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setBackgroundColor(Color.WHITE)

        tabLayout.setupWithViewPager(viewPager)



    }



    override fun onDetach() {
        super.onDetach()
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private var bestPlaceFragment: BestPlaceFragment? = null
        private var reviewFragment: ReviewFragment? = null

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position) {
                0 -> {
                    if(bestPlaceFragment == null) {
                        bestPlaceFragment = BestPlaceFragment()
                    }

                    return bestPlaceFragment!!
                }
                1 -> {
                    if(reviewFragment == null) {
                        reviewFragment = ReviewFragment()
                    }

                    return reviewFragment!!
                }
            }


            Log.e("position error", "" + position)
            return bestPlaceFragment!!

        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return resources.getString(R.string.best_view_pager_best)
                1 -> return resources.getString(R.string.best_view_pager_review)
            }
            return null
        }
    }


}
