package com.hyunstyle.inhapet.thread;

import android.os.AsyncTask;
import android.util.Log;

//import com.hyunstyle.inhapet.model.Restaurant;

import com.hyunstyle.inhapet.Util;
import com.hyunstyle.inhapet.interfaces.AsyncTaskResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sh on 2018-04-04.
 */

public class DataDownloadingThread extends AsyncTask<String, Void, JSONArray> {

    private AsyncTaskResponse delegate;

    public DataDownloadingThread(AsyncTaskResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JSONArray doInBackground(String... u) {

        String result = "";
        JSONArray jsonArray = null;

        try {

            //Log.e("uuuu", u[0] + " " + u[1] + " " + u[2]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(u[0]);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair(u[1], u[2])); // hi_client , unique id
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, "utf-8"));
            HttpResponse httpResponse = httpclient.execute(httpPost);

            InputStream inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null)
                result = Util.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            Log.e("result",result);

            // convert json format to key / value.
            jsonArray = new JSONArray(result);

            Log.e("length", "" + jsonArray.length());

            return jsonArray;

        } catch (Exception e){
            e.printStackTrace();
        }

        return jsonArray;


    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {

        delegate.finished(jsonArray);
        super.onPostExecute(jsonArray);
    }

}
