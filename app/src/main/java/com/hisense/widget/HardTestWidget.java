package com.hisense.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hisense.R;
import com.hisense.utils.ComnUtils;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Implementation of App Widget functionality.
 */
public class HardTestWidget extends AppWidgetProvider {

    private static final String TAG = "HardTestWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "HardTestWidget onUpdate");
        Intent actClick = new Intent("com.zwq.hardtesttask");
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, actClick, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.hard_test_widget);
        views.setOnClickPendingIntent(R.id.img_hardManager, pending);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.d(TAG, action);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if ("com.zwq.hardtesttask".equals(action)) {
            if (ComnUtils.getAppVersion(context, "com.hisense.hk716hardmanager") != null) {
                Intent intentJump = new Intent(Intent.ACTION_MAIN);
                //前提：知道要跳转应用的包名、类名
                ComponentName componentName = new ComponentName("com.hisense.hk716hardmanager", "com.hisense.hk716hardmanager.MainActivity");
                intentJump.setComponent(componentName);
                intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentJump);
            } else {
                Toast.makeText(context, "NO APP", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

