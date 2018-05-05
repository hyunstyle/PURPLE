package com.hyunstyle.inhapet

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.hyunstyle.inhapet.util.SquareFrameLayout
import com.hyunstyle.inhapet.util.SquareImageView
import java.util.*

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
    private lateinit var selectedImageRecyclerView: RecyclerView
    private var selectedImagesAdapter: SelectedImagesAdapter? = null

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
        selectedImageRecyclerView = findViewById(R.id.review_selected_images_recycler_view)
        selectedImageRecyclerView.layoutManager = LinearLayoutManager(this@ReviewActivity, LinearLayoutManager.HORIZONTAL, false)
        selectedImageRecyclerView.setHasFixedSize(true)

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

            val intent = Intent(this, GalleryActivity::class.java)
            startActivityForResult(intent, 200)
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_GET_CONTENT
//            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
//            startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.review_select_if)), 100)
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
                val intent = Intent(this, GalleryActivity::class.java)
                startActivityForResult(intent, 200)
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                intent.action = Intent.ACTION_GET_CONTENT
//                intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
//                startActivityForResult(Intent.createChooser(intent, resources.getString(R.string.review_select_if)), 100)

            } else {
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 200) {
            if(resultCode == Activity.RESULT_OK) {
                var selectedImageList = data!!.getParcelableArrayListExtra<Uri>(resources.getString(R.string.review_image_uris))

                Log.e("length", "" + selectedImageList.size)

                selectedImagesAdapter = SelectedImagesAdapter(this)
                selectedImageRecyclerView.adapter = selectedImagesAdapter
                selectedImagesAdapter!!.setData(selectedImageList)

            }
        }
    }


    inner class ImageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        var thumbnail: SquareImageView?
        var layout: SquareFrameLayout?
        var uri: Uri? = null

        init {
            layout = view.findViewById(R.id.container)
            thumbnail = view.findViewById(R.id.thumbnail_image_view)
        }
    }

    inner class SelectedImagesAdapter(private val context: Context) : RecyclerView.Adapter<ImageViewHolder>() {

        private var imageList: List<Uri> = Collections.emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gallery_thumbnail, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

            if (holder.adapterPosition != RecyclerView.NO_POSITION) {

                val mUri = imageList[holder.adapterPosition]
                if (holder.layout != null) {
                    //holder.layout.foreground = (if (isSelected) ResourcesCompat.getDrawable(resources, R.drawable.shape_selected, null) else null)
//                    holder.layout.setOnClickListener({ view ->
//                        if (!containsImage(mUri))
//                            addImage(mUri)
//                        else
//                            removeImage(mUri)
//                        galleryAdapter.notifyItemChanged(holder.adapterPosition)
//                    })
                }

                if (holder.uri == null || holder.uri != mUri) {

                    GlideApp.with(context)
                            .load(mUri.toString())
                            .thumbnail(0.1f)
                            //.dontAnimate()
                            .centerCrop()
                            .placeholder(R.drawable.ic_outside_meal)
                            .error(R.drawable.ic_close_black_24dp)
                            .into(holder.thumbnail as ImageView)
                    holder.uri = mUri
                }
            }
        }

        fun setData(data: MutableList<Uri>) {
            this.imageList = data
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        override fun getItemId(position: Int): Long {
            return super.getItemId(position)
        }
    }


}
