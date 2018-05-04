package com.hyunstyle.inhapet;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyunstyle.inhapet.util.DynamicLoadingGridView;
import com.hyunstyle.inhapet.util.SquareFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh on 2018-05-04.
 */

public class GalleryActivity extends AppCompatActivity implements DynamicLoadingGridView.OnEndScrollListener {

    public ImageGalleryAdapter galleryAdapter;
    private GalleryActivity activity;
    private final int selectionLimit = 10; // # of max image selection

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
        DynamicLoadingGridView galleryGridView = findViewById(R.id.gallery_grid);
        galleryGridView.setOnEndScrollListener(this);

        imagesUriList = getImagesFromGallery(this, imageCursor);
        galleryAdapter = new ImageGalleryAdapter(this, imagesUriList);

        galleryGridView.setAdapter(galleryAdapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Uri mUri = galleryAdapter.getItem(i);
                if (!containsImage(mUri))
                    addImage(mUri);
                else
                    removeImage(mUri);
                galleryAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onEndScroll() {
        List<Uri> plusLoadedList = getImagesFromGallery(this, imageCursor);

        if(plusLoadedList.size() != 0) {
            galleryAdapter.addData(plusLoadedList);
        }
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


    public List<Uri> getImagesFromGallery(Context context, Cursor imageCursor) {

        List<Uri> images = new ArrayList<>();
        boolean isFinal = false;

        try {
            if(!isEndOfGallery) {
                if (imageCursor == null) {
                    final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
                    final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
                    imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                }

                for (int i = 0; i < 24; i++) {
                    if (!imageCursor.isAfterLast()) {
                        imageCursor.moveToNext();
                        Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));
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
                isEndOfGallery = true;
                imageCursor.close();
            }
        }

        return images;
    }

    class ViewHolder {
        SquareFrameLayout layout;
        ImageView thumbnail;
        Uri uri;

        public ViewHolder(View view) {
            layout = view.findViewById(R.id.container);
            thumbnail = view.findViewById(R.id.thumbnail_image_view);
        }
    }

    // TODO : ListView -> RecyclerView 바꿀것
    public class ImageGalleryAdapter extends BaseAdapter {

        Context context;
        List<Uri> imageList;

        public ImageGalleryAdapter(Context context, List<Uri> images) {
            this.imageList = images;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Uri getItem(int i) {
            return imageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_gallery_thumbnail, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Uri mUri = getItem(position);
            boolean isSelected = activity.containsImage(mUri);

            //Log.e("selected", "pos:" + position + " is" + isSelected);
            if(holder.layout != null){
                (holder.layout).setForeground(isSelected ? ResourcesCompat.getDrawable(getResources(),R.drawable.shape_selected,null) : null);
            }

            if (holder.uri == null || !holder.uri.equals(mUri)) {

                GlideApp.with(context)
                        .load(mUri.toString())
                        .thumbnail(0.1f)
                        .dontAnimate()
                        .centerCrop()
                        .placeholder(R.drawable.ic_outside_meal)
                        .error(R.drawable.ic_close_black_24dp)
                        .into(holder.thumbnail);
                holder.uri = mUri;
            }

            return convertView;
        }

        public void addData(List<Uri> addedList) {
            this.imageList.addAll(addedList);
            notifyDataSetChanged();
        }
    }
}
