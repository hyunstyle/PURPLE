package com.hyunstyle.inhapet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide

/**
 * Created by sh on 2018-05-04.
 */

class ReviewActivity : AppCompatActivity() {

    private lateinit var shopNameTextView: TextView
    private lateinit var backArrowButton: ImageButton
    private lateinit var finishButton: ImageButton
    private lateinit var ratingBar: RatingBar
    private lateinit var ratingBarTextView: TextView
    private lateinit var galleryButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_review)

        init()
    }

    private fun init() {
        shopNameTextView = findViewById(R.id.review_shop_name)
        shopNameTextView.text = intent.getStringExtra(resources.getString(R.string.map_shop_name))
        backArrowButton = findViewById(R.id.review_back_button)
        finishButton = findViewById(R.id.review_finish_button)
        ratingBar = findViewById(R.id.review_rating_bar)
        galleryButton = findViewById(R.id.review_gallery)
        Glide.with(this)
                .load(ContextCompat.getDrawable(this, R.drawable.ic_add_24dp))
                .into(galleryButton)

        galleryButton.setOnClickListener { click -> kotlin.run {
            getGalleryPermission()
        } }

        ratingBarTextView = findViewById(R.id.review_rating_text)
        ratingBarTextView.text = resources.getString(R.string.review_rating_point, ratingBar.rating.toString())

        ratingBar.setOnRatingBarChangeListener { rb, fl, b -> kotlin.run {
            Toast.makeText(this, "rating: " + rb.rating, Toast.LENGTH_SHORT).show()
            ratingBarTextView.text = resources.getString(R.string.review_rating_point, ratingBar.rating.toString())
        } }
//        val stars = ratingBar.progressDrawable
//        stars.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
    }

    private fun getGalleryPermission() {
        val writePermission = ContextCompat.checkSelfPermission(this@ReviewActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val readPermission = ContextCompat.checkSelfPermission(this@ReviewActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        if(writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@ReviewActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 200)

        } else {
            Log.e("permission", "있음")

            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.review_select_if)), 100)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 200) {
            var isGranted = true
            for(result in grantResults) {
                Log.e("result", "" + result + ", grant: " + PackageManager.PERMISSION_GRANTED)
                if(result != PackageManager.PERMISSION_GRANTED)
                    isGranted = false
            }

            if(isGranted) {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.review_select_if)), 100)

            } else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100) {
            if(resultCode == Activity.RESULT_OK) {

            }
        }
    }


}
