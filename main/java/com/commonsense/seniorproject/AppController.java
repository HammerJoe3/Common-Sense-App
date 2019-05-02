package com.commonsense.seniorproject;

/**
 *  Default class that enables Volley to work properly
 */

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.android.volley.Request.Method.POST;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public String futureRequestInWidget() {
        //tag used to cancel the request
        String tag_string_req = "req_news";

        RequestFuture<String> future = RequestFuture.newFuture();
        StringRequest request = new StringRequest(POST, CommonSenseConfig.URL_NEWS, future, future);

        AppController.getInstance().addToRequestQueue(request, tag_string_req);

        try {
            //return future.get(30, TimeUnit.SECONDS);
            String response = future.get(5, TimeUnit.SECONDS);

            try {
                JSONObject jObj = new JSONObject(response);

                return jObj.getString("News");
            } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            }

        } catch (InterruptedException e) {
            Log.e("api call interrupted.", "" + e);
        } catch (ExecutionException e) {
            Log.e("api call failed.", "" + e);
        } catch (TimeoutException e) {
            Log.e("call timed out.", "" + e);
        }
        return null;
    }

}
