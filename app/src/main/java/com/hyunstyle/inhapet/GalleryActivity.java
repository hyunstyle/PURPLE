package com.hyunstyle.inhapet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyunstyle.inhapet.util.DynamicLoadingGridView;
import com.hyunstyle.inhapet.util.DynamicLoadingRecyclerView;
import com.hyunstyle.inhapet.util.SquareFrameLayout;
import com.hyunstyle.inhapet.util.SquareImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sh on 2018-05-04.
 */

public class GalleryActivity extends AppCompatActivity implements DynamicLoadingRecyclerView.OnEndScrollListener {

    public GalleryAdapter galleryAdapter;
    private GalleryActivity activity;
    private final int selectionLimit = 10; // # of max image selection
    private DynamicLoadingRecyclerView galleryRecyclerView;
    private GridScrollLayoutManager galleryRecyclerViewLayoutManager;

    public ArrayList<Uri> selectedImagesList;
    private Cursor imageCursor = null;
    private boolean isEndOfGallery = false;
    private List<Uri> imagesUriList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromSavedInstanceState(savedInstanceState);
        setContentView(R.layout.content_gallery);

        activity = this;
        galleryRecyclerView = findViewById(R.id.gallery_recycler_view);
        galleryRecyclerViewLayoutManager = new GridScrollLayoutManager(this, 3, GridScrollLayoutManager.VERTICAL, false);
        galleryRecyclerView.setLayoutManager(galleryRecyclerViewLayoutManager);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setOnEndScrollListener(this);

        imagesUriList = getImagesFromGallery(this);
        Log.e("length..", "" + imagesUriList.size());
        galleryAdapter = new GalleryAdapter(this);
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryAdapter.setData(imagesUriList);
        ImageButton finishButton = findViewById(R.id.gallery_finish_button);
        finishButton.setOnClickListener(view -> {
            if(selectedImagesList.size() > 0) {
                Intent intent = new Intent();
                intent.putExtra(getResources().getString(R.string.review_image_uris), selectedImagesList);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, getResources().getString(R.string.gallery_toast), Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * back up code(ListView)
         */
        //DynamicLoadingGridView galleryGridView = findViewById(R.id.gallery_grid);
//        galleryGridView.setOnEndScrollListener(this);
        //galleryAdapter.setHasStableIds(true);
//        List<Uri> plusLoadedList = getImagesFromGallery(activity);
//        if(plusLoadedList.size() != 0) {
//            galleryAdapter.addData(plusLoadedList);
//        }
//        galleryGridView.setAdapter(galleryAdapter);
//        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Uri mUri = galleryAdapter.getItem(i);
//                if (!containsImage(mUri))
//                    addImage(mUri);
//                else
//                    removeImage(mUri);
//                galleryAdapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public void onEndScroll() {
//        List<Uri> plusLoadedList = getImagesFromGallery(this);
//
//        if(plusLoadedList.size() != 0) {
//            galleryRecyclerView.post(() -> galleryAdapter.addData(plusLoadedList));
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        if (selectedImagesList != null) {
            bundle.putParcelableArrayList(getResources().getString(R.string.review_image_uris), selectedImagesList);
        }
    }

    private void loadFromSavedInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            selectedImagesList = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.review_image_uris));
        } else {
            selectedImagesList = getIntent().getParcelableArrayListExtra(getResources().getString(R.string.review_image_uris));
        }

        if (selectedImagesList == null) {
            selectedImagesList = new ArrayList<>();
        }
    }

    public boolean containsImage(Uri uri) {
        return selectedImagesList.contains(uri);
    }

    public void addImage(final Uri uri) {

        if (selectedImagesList.size() == selectionLimit) {
            Toast.makeText(this, "max select", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedImagesList.add(uri);
        Log.e("list size", "" + selectedImagesList.size());

    }

    public void removeImage(Uri uri) {
        selectedImagesList.remove(uri);
        galleryAdapter.notifyDataSetChanged();
    }


    public List<Uri> getImagesFromGallery(Context context) {

        List<Uri> images = new ArrayList<>();
        boolean isFinal = false;

        try {
            if(!isEndOfGallery) {
                if (imageCursor == null) {
                    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
                    final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                    imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

                    Log.e("cursor", "null");
                }

                while(true) {
                    if (!imageCursor.isAfterLast()) {
                        imageCursor.moveToNext();
                        Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        //Log.e("uri", uri.toString());
                        images.add(uri);
                    } else {
                        isFinal = true;
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isFinal) {
                if(!imageCursor.isClosed()) {
                    isEndOfGallery = true;
                    imageCursor.close();
                }
            }
        }

        return images;
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView thumbnail;
        private SquareFrameLayout layout;
        private Uri uri;

        protected ImageViewHolder(View view) {
            super(view);

            layout = view.findViewById(R.id.container);
            thumbnail = view.findViewById(R.id.thumbnail_image_view);
        }
    }

    public class GalleryAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        private List<Uri> imageList = Collections.emptyList();
        private Context context;

        public GalleryAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gallery_thumbnail, parent, false);

            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {

            if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION) {

                final Uri mUri = imageList.get(holder.getAdapterPosition());
                boolean isSelected = activity.containsImage(mUri);
                Log.e("selected", "pos:" + holder.getAdapterPosition() + " is" + isSelected);
                if (holder.layout != null) {
                    (holder.layout).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(), R.drawable.shape_selected, null) : null);
                    holder.layout.setOnClickListener(view -> {
                        if (!containsImage(mUri))
                            addImage(mUri);
                        else
                            removeImage(mUri);
                        galleryAdapter.notifyItemChanged(holder.getAdapterPosition());
                    });
                }

                if (holder.uri == null || !holder.uri.equals(mUri)) {

                    GlideApp.with(context)
                            .load(mUri.toString())
                            .thumbnail(0.1f)
                            //.dontAnimate()
                            .centerCrop()
                            .placeholder(R.drawable.ic_outside_meal)
                            .error(R.drawable.ic_close_black_24dp)
                            .into(holder.thumbnail);
                    holder.uri = mUri;
                }
            }
        }

        public void setData(List<Uri> data) {
            this.imageList = data;
            notifyDataSetChanged();
        }

        public void addData(List<Uri> data) {
            Log.e("adddata", "" + data.size());
            int prevSize = imageList.size() - 1;

            imageList.addAll(data);
            Log.e("size", "" + imageList.size());
            notifyItemRangeInserted(prevSize, data.size());
        }


        @Override
        public int getItemCount() {
            return imageList.size();
        }
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }
    }


    /**
     * back up code(ListView)
     */
    //    class ViewHolder {
//        SquareFrameLayout layout;
//        ImageView thumbnail;
//        Uri uri;
//
//        public ViewHolder(View view) {
//            layout = view.findViewById(R.id.container);
//            thumbnail = view.findViewById(R.id.thumbnail_image_view);
//        }
//    }
//    public class ImageGalleryAdapter extends BaseAdapter {
//
//        Context context;
//        List<Uri> imageList;
//
//        public ImageGalleryAdapter(Context context, List<Uri> images) {
//            this.imageList = images;
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return imageList.size();
//        }
//
//        @Override
//        public Uri getItem(int i) {
//            return imageList.get(i);
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final ViewHolder holder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_gallery_thumbnail, null);
//                holder = new ViewHolder(convertView);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            final Uri mUri = getItem(position);
//            boolean isSelected = activity.containsImage(mUri);
//
//            //Log.e("selected", "pos:" + position + " is" + isSelected);
//            if(holder.layout != null){
//                (holder.layout).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(),R.drawable.shape_selected,null) : null);
//            }
//
//            if (holder.uri == null || !holder.uri.equals(mUri)) {
//
//                GlideApp.with(context)
//                        .load(mUri.toString())
//                        .thumbnail(0.1f)
//                        .dontAnimate()
//                        .centerCrop()
//                        .placeholder(R.drawable.ic_outside_meal)
//                        .error(R.drawable.ic_close_black_24dp)
//                        .into(holder.thumbnail);
//                holder.uri = mUri;
//            }
//
//            return convertView;
//        }
//
//        public void addData(List<Uri> addedList) {
//            this.imageList.addAll(addedList);
//            notifyDataSetChanged();
//        }
//    }
}
