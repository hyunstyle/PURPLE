package com.hyunstyle.inhapet.thread;

import android.os.AsyncTask;

import com.hyunstyle.inhapet.interfaces.AsyncTaskResponse;

import org.json.JSONArray;

/**
 * Created by sh on 2018-05-16.
 */

public class GetReviewListThread extends AsyncTask<String, Void, JSONArray> {

    private AsyncTaskResponse delegate;
    private int shopId;

    public GetReviewListThread(AsyncTaskResponse delegate, int shopId) {
        this.delegate = delegate;
        this.shopId = shopId;
    }

    @Override
    protected JSONArray doInBackground(String... strings) {




        return null;
    }
}
