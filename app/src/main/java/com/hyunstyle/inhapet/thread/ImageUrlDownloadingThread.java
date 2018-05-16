package com.hyunstyle.inhapet.thread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SangHyeon on 2018-04-09.
 */

public class ImageUrlDownloadingThread extends AsyncTask<String, Void, JSONArray> {

    private AsyncTaskResponse delegate;

    public ImageUrlDownloadingThread(AsyncTaskResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected JSONArray doInBackground(String... u) {


        if(u.length != 2)
            return null;

        String result = "";
        JSONArray jsonArray = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(u[0]);

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair(u[1], u[0])); // no meaning
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
    protected void onPostExecute(JSONArray jsonArray)
    {
        if(jsonArray != null) {
            delegate.finished(jsonArray, 0);
        }
    }
}
