package com.commonsense.seniorproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.content.ContextCompat.startActivity;


public class ListProvider implements RemoteViewsFactory {
    private ArrayList<News> listItemList = new ArrayList<News>();
    private Context context = null;
    private int appWidgetId;
    private String articleString;

    private NewsFeed news;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();

    }



    private void populateListItem() {
        //tag used to cancel the request
        String tag_string_req = "req_news";

        StringRequest strReq = new StringRequest(Request.Method.POST, CommonSenseConfig.URL_NEWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse", "news response: " + response);


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in JSON
                    if (!error) {

                        String database_news_list = jObj.getString("News");
                        String test = jObj.getString("News");
                        storeNews(test);

                    } else {
                        // error in login, get Error Message
                    }
                } catch (JSONException e) {
                    // JSON error
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to news url
                Map<String, String> params = new HashMap<String, String>();

                //params.put("userID",session.getUserID());

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        //int i = -1;
    }

    private void populateWidget(){
        NewsFeed news = new NewsFeed();
        news = news.getArticles(articleString);
        ArrayList<News> list = new ArrayList<News>();
        list =news.getNewsList();
        for (int i= 0; i< 10; i++){
            listItemList.add(list.get(i));
        }
        listItemList.add(new News("viewmore","Click Here To View More Articles", ""));
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    private void storeNews(String news) {
        this.articleString = news;
        populateWidget();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        News listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.content, listItem.articleName);
        Intent fillIntent = new Intent();
        Bundle extra = new Bundle();
        extra.putString(WidgetProvider.EXTRA_ITEM_POSTION,listItem.link);
        fillIntent.putExtras(extra);
        remoteView.setOnClickFillInIntent(R.id.content,fillIntent);


        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
