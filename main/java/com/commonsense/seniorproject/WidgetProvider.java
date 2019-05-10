package com.commonsense.seniorproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;
/*
Created By Dylan Shapiro 

*/



public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOAST = "actionToast";
    public static final String EXTRA_ITEM_POSTION = "extraItemPostion";
    /*
     * this method is called every 30 mins as specified on widgetinfo.xml
     * this method is also called on every phone reboot
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        /*int[] appWidgetIds holds ids of multiple instance of your widget
         * meaning you are placing more than one widgets on your homescreen*/
        for (int i = 0; i < N; ++i) {
            RemoteViews remoteViews = updateWidgetListView(context,
                    appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);


        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);

        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
                svcIntent);
        Intent  clickIntent = new Intent(context, WidgetProvider.class);
        clickIntent.setAction(ACTION_TOAST);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0,clickIntent,0);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        remoteViews.setPendingIntentTemplate(R.id.listViewWidget, clickPendingIntent);
        return remoteViews;
    }
    
    
    /*
        this method is for when a click on the widget is registered it handles it accordingly
    */    
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION_TOAST.equals(intent.getAction())) {


            String link = intent.getStringExtra(EXTRA_ITEM_POSTION);
            if(!(link.equals("viewmore"))) {
                try {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(webIntent);
                } catch (RuntimeException e) {
                    // The url is invalid, maybe missing http://
                    e.printStackTrace();
                }


                Toast.makeText(context, link, Toast.LENGTH_LONG).show();
            }
            else {
                Intent intn = new Intent (context, NavigationActivity.class);
                intn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intn);
            }
        }
       
    }

    private void openLink(Context context) {
    }
}
