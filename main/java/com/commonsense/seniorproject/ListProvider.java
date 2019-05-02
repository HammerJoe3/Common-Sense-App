package com.commonsense.seniorproject;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


public class ListProvider implements RemoteViewsFactory {
    private ArrayList<News> listItemList = new ArrayList<News>();
    private Context context;
    private int appWidgetId;

    private String database_news_list;

    public ListProvider(Context context, Intent intent, String result) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     */
    @Override
    public RemoteViews getViewAt(final int position) {

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

        AppController appController = new AppController();
        database_news_list = appController.futureRequestInWidget();
        Log.e("CHECKING RESULT", " " + database_news_list);
        populateListItem(database_news_list);


        Log.e("TestDataSetChanged", "" + database_news_list);

    }

    @Override
    public void onDestroy() {
    }

    private void populateListItem(String result) {
        NewsFeed news = new NewsFeed();
        news = news.getArticles(result);
        ArrayList<News> list = new ArrayList<News>();
        list =news.getNewsList();
        for (int i= 0; i< 10; i++){
            listItemList.add(list.get(i));
        }
        listItemList.add(new News("viewmore","Click Here To View More Articles", ""));
    }
}
