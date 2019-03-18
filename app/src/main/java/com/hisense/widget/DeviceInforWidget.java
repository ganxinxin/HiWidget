package com.hisense.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.hisense.R;
import com.hisense.utils.AndroidUtils;

/**
 * Implementation of App Widget functionality.
 */
public class DeviceInforWidget extends AppWidgetProvider {

    private static final String TAG = "DeviceInforWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews updateDeviceInforView = new RemoteViews(context.getPackageName(), R.layout.device_infor_widget);
        //根据机型，显示logo
        if ("HM618".equals(AndroidUtils.getDeviceModel()) || "HM616".equals(AndroidUtils.getDeviceModel())) {
            updateDeviceInforView.setImageViewResource(R.id.img_logo, R.drawable.logo_hm616);
        } else if ("HS6500A".equals(AndroidUtils.getDeviceModel()) || "HS6500R".equals(AndroidUtils.getDeviceModel())) {
            updateDeviceInforView.setImageViewResource(R.id.img_logo, R.drawable.logo_hs6500);
        } else if ("HT5000".equals(AndroidUtils.getDeviceModel())) {
            updateDeviceInforView.setImageViewResource(R.id.img_logo, R.drawable.logo_ht5000);
        } else {
            updateDeviceInforView.setImageViewResource(R.id.img_logo, R.drawable.logo_hk716);
        }
        updateDeviceInforView.setTextViewText(R.id.tv_devType, AndroidUtils.getDeviceModel());
        updateDeviceInforView.setTextViewText(R.id.tv_locate, "青岛海信广场东海路店 1F-10");
        updateDeviceInforView.setTextViewText(R.id.tv_sn, "展示样机");
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, DeviceInforWidget.class), updateDeviceInforView);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

