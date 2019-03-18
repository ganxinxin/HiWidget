package com.hisense.widget;



import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Handler;
import android.os.Message;
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
public class DeviceStatusWidget extends AppWidgetProvider {

    private static final String TAG = "DeviceStatusWidget";
    private Bitmap iconNormal = null;
    private Bitmap iconAbNormal = null;


    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // There may be multiple widgets active, so update all of them
        iconNormal = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.normal)).getBitmap();
        iconAbNormal = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.abnormal)).getBitmap();
        RemoteViews updateDeviceStatusView = new RemoteViews(context.getPackageName(), R.layout.device_status_widget);
        //设备点击
        Intent devicestatusClick = new Intent("com.zwq.systatus");
        PendingIntent lightaddPending = PendingIntent.getBroadcast(context, 0, devicestatusClick, 0);
        updateDeviceStatusView.setOnClickPendingIntent(R.id.tv_device, lightaddPending);
        //网络点击
        Intent networkstatusClick = new Intent("com.zwq.networkstatus");
        PendingIntent lightsubtractPending = PendingIntent.getBroadcast(context, 0, networkstatusClick, 0);
        updateDeviceStatusView.setOnClickPendingIntent(R.id.tv_network, lightsubtractPending);
        //存储点击
        Intent storestatusClick = new Intent("com.zwq.systatus");
        PendingIntent volumeaddPending = PendingIntent.getBroadcast(context, 0, storestatusClick, 0);
        updateDeviceStatusView.setOnClickPendingIntent(R.id.tv_sd, volumeaddPending);
        //打印机点击
        Intent printerstatusClick = new Intent("com.zwq.printerstatus");
        PendingIntent volumesubtractPending = PendingIntent.getBroadcast(context, 0, printerstatusClick, 0);
        updateDeviceStatusView.setOnClickPendingIntent(R.id.tv_printer, volumesubtractPending);
        //内存点击
        Intent memstatusClick = new Intent("com.zwq.systatus");
        PendingIntent silenttaskPending = PendingIntent.getBroadcast(context, 0, memstatusClick, 0);
        updateDeviceStatusView.setOnClickPendingIntent(R.id.tv_mem, silenttaskPending);
        appWidgetManager.updateAppWidget(new ComponentName(context, DeviceStatusWidget.class), updateDeviceStatusView);
        // 开始定时发送更新广播
        GlobalData.pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendDeviceStatusMessage(context);
            }
        }, 0, 10000, TimeUnit.MILLISECONDS);
    }

    private void sendDeviceStatusMessage(Context context) {

        RemoteViews updateDeviceStatusView = new RemoteViews(context.getPackageName(), R.layout.device_status_widget);
         /*内存状态设置
        */
        float memPercent = (float) (AndroidUtils.getTotalRAM(context) - AndroidUtils.getAvailRAM(context)) / (AndroidUtils.getTotalRAM(context));
        if (memPercent <= 0.8) {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_mem, iconNormal);
        } else {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_mem, iconAbNormal);
        }
        /*存储状态设置
        */
        float storagePercent = (float) (AndroidUtils.getTotalStorge() - AndroidUtils.getAvailableStorge()) / (AndroidUtils.getTotalStorge());
        if (storagePercent <= 0.9) {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_sd, iconNormal);
        } else {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_sd, iconAbNormal);
        }
        /*网络状态设置
        */
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取ConnectivityManager对象对应的NetworkInfo对象 //获取WIFI连接的信息
        NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // /获取移动数据连接的信息
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_network, iconNormal);
        } else {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_network, iconAbNormal);
        }
        if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_network, iconNormal);
        } else {
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_network, iconAbNormal);
        }
        /*打印机状态设置
        */
        try {
            GlobalData.printer.printerConfig(115200, true);
            Log.d(TAG, "printerConfig try");
            GlobalData.printer.initPrinter(0);
            Log.d(TAG, "initPrinter");
            if (GlobalData.printer.printStatus() == 0) {
                //正常
                updateDeviceStatusView.setImageViewBitmap(R.id.tv_printer, iconNormal);
            } else if (GlobalData.printer.printStatus() == 1) {
                //缺纸
                updateDeviceStatusView.setImageViewBitmap(R.id.tv_printer, iconAbNormal);
            } else {
                //负值，异常
                updateDeviceStatusView.setImageViewBitmap(R.id.tv_printer, iconAbNormal);
            }
        } catch (Exception e) {
            Log.d(TAG, "printerConfig catch");
            //异常
            updateDeviceStatusView.setImageViewBitmap(R.id.tv_printer, iconAbNormal);
            e.printStackTrace();
        }
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, DeviceStatusWidget.class), updateDeviceStatusView);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.d(TAG, action);
        if (ComnUtils.getAppVersion(context, "com.hisense.hk716hardmanager") != null) {
            if (action.equals("com.zwq.systatus")) {
                if ("HK316R".equals(AndroidUtils.getDeviceModel())) {
                    Intent intentJump = new Intent(Intent.ACTION_MAIN);
                    ComponentName componentName = new ComponentName("com.hisense.hk716hardmanager", "com.hisense.hk716hardmanager.ItemListActivity");
                    intentJump.setComponent(componentName);
                    intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentJump);
                } else {
                    Intent intentJump = new Intent(Intent.ACTION_MAIN);
                    ComponentName componentName = new ComponentName("com.hisense.hk716hardmanager", "com.hisense.hk716hardmanager.SystemInfo");
                    intentJump.setComponent(componentName);
                    intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentJump);
                }
            } else if (action.equals("com.zwq.networkstatus")) {
                Intent intentJump = new Intent(Intent.ACTION_MAIN);
                //前提：知道要跳转应用的包名、类名
                ComponentName componentName = new ComponentName("com.hisense.hk716hardmanager", "com.hisense.hk716hardmanager.NetWorkActivity");
                intentJump.setComponent(componentName);
                intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentJump);

            } else if (action.equals("com.zwq.printerstatus")) {
                Intent intentJump = new Intent(Intent.ACTION_MAIN);
                //前提：知道要跳转应用的包名、类名
                ComponentName componentName = new ComponentName("com.hisense.hk716hardmanager", "com.hisense.hk716hardmanager.PrinterActivity");
                intentJump.setComponent(componentName);
                intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentJump);

            }
        }
    }

}


