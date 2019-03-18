package com.hisense.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
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
public class BootRunningWidget extends AppWidgetProvider {

    private static final String TAG = "BootRunningWidget";


    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // 开始定时发送更新广播
        GlobalData.pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendBootRunningMessage(context);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void sendBootRunningMessage(Context context) {
        Log.e(TAG, "sendBootRunningMessage");
        RemoteViews updateBootRunningView = new RemoteViews(context.getPackageName(), R.layout.boot_running_widget);
        updateBootRunningView.setTextViewText(R.id.tv_runTime, ComnUtils.getTime(Integer.valueOf(AndroidUtils.getRunTime())));
        updateBootRunningView.setTextViewText(R.id.tv_upSpeed, ComnUtils.getNetUpSpeed());
        updateBootRunningView.setTextViewText(R.id.tv_downSpeed, ComnUtils.getNetDownSpeed());
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, BootRunningWidget.class), updateBootRunningView);
    }

}

