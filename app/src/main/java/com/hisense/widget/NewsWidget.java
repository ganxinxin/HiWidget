package com.hisense.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.RemoteViews;
import com.hisense.R;
import com.hisense.utils.AndroidUtils;
import com.hisense.utils.ComnUtils;
import com.hisense.utils.GlobalData;

import java.util.concurrent.TimeUnit;


/**
 * Implementation of App Widget functionality.
 */
public class NewsWidget extends AppWidgetProvider {

    private static final String TAG = "NewsWidget";

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        // 开始定时发送更新广播
        GlobalData.pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendNewsMessage(context);
            }
        }, 0, 60000, TimeUnit.MILLISECONDS);
    }

    private void sendNewsMessage(Context context) {
        Log.e(TAG, "sendNewsMessage");
        RemoteViews updateNewsView = new RemoteViews(context.getPackageName(), R.layout.news_widget);
        updateNewsView.setTextViewText(R.id.time_news, "欢迎光临！");
        //获取ConnectivityManager对象对应的NetworkInfo对象 //获取WIFI连接的信息
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // /获取移动数据连接的信息
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
            if (wifiNetworkInfo.isConnected())
                updateNewsView.setTextViewText(R.id.hard_news, "网络已连接,WIFI连接");
            else
                updateNewsView.setTextViewText(R.id.hard_news, "网络已连接，移动数据连接");
        } else {
            updateNewsView.setTextViewText(R.id.hard_news, "网络未连接，请检查网络状态！");
        }
        updateNewsView.setTextViewText(R.id.notice_news, "应用商城，2款应用可更新");
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, NewsWidget.class), updateNewsView);
    }

}

