package com.hyunstyle.inhapet.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hyunstyle.inhapet.GlideApp
import com.hyunstyle.inhapet.Util
import com.hyunstyle.inhapet.util.RatioImageView
import jp.wasabeef.glide.transformations.CropTransformation

import java.util.ArrayList

/**
 * Created by SangHyeon on 2018-04-08.
 */

class TopAdViewPagerAdapter(val context: Context, private val urls: ArrayList<String>) : PagerAdapter() {

    //private var imageArrayList: List<Bitmap> = ArrayList()

    override fun getCount(): Int {
        //Log.e("getcount", "" + urls.size)
        return urls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        Log.e("instanciate", urls[position])
        val i = ImageView(context)

        var params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
        params.dimensionRatio = "H,4:1"

        i.adjustViewBounds = true
        i.scaleType = ImageView.ScaleType.CENTER_CROP
        i.cropToPadding = true
        i.layoutParams = params

        GlideApp.with(context)
                .load(urls[position])
//                .apply(RequestOptions.bitmapTransform(CropTransformation(Util.dip2px(context, 300f), Util.dip2px(context, 100f),
//                        CropTransformation.CropType.TOP)))
                .into(i)



        container.addView(i)

        //(i.drawable as BitmapDrawable).bitmap

        return i

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
