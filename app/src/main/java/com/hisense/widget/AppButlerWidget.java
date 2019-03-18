package com.hisense.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hisense.R;
import com.hisense.utils.AndroidUtils;
import com.hisense.utils.ComnUtils;


/**
 * Implementation of App Widget functionality.
 */
public class AppButlerWidget extends AppWidgetProvider {

    private static final String TAG = "AppButlerWidget";
    private Context context = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "AppButlerWidget onUpdate");
        this.context = context;

        Intent actClick = new Intent("com.zwq.appbutlertask");
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, actClick, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_butler_widget);
        views.setOnClickPendingIntent(R.id.img_appbulter, pending);
        views.setImageViewBitmap(R.id.img_appbulter, setTextToImg(2));
        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.d(TAG, action);
        if ("com.zwq.appbutlertask".equals(action)) {
            if (ComnUtils.getAppVersion(context, "com.hisense.appstore") != null) {
                Intent intentJump = new Intent(Intent.ACTION_MAIN);
                //前提：知道要跳转应用的包名、类名
                ComponentName componentName = new ComponentName("com.hisense.appstore", "com.hisense.appstore.activity.AppActivity");
                intentJump.setComponent(componentName);
                intentJump.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentJump);
            } else {
                Toast.makeText(context, "NO APP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 文字绘制在图片上，并返回bitmap对象
     */
    private Bitmap setTextToImg(int contacyCount) {
        Log.d(TAG, "AppButlerWidget setTextToImg");

        BitmapDrawable icon = (BitmapDrawable) context.getResources().getDrawable(R.drawable.main_appstore);
        Bitmap contactIcon = Bitmap.createBitmap(icon.getBitmap().getWidth(), icon.getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
        //将该图片作为画布
        Canvas canvas = new Canvas(contactIcon);
        //在画布 0，0坐标上开始绘制原始图片
        Paint iconPaint = new Paint();
        iconPaint.setDither(true);//防抖动
        iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
        canvas.drawBitmap(icon.getBitmap(), 0, 0, iconPaint);

        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setTextSize(20f);
        countPaint.setColor(Color.parseColor("#D01B1B"));
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //canvas.drawText(String.valueOf(contacyCount),(contactIcon.getWidth() / 5),(contactIcon.getHeight() / 2), countPaint);
        canvas.drawText(String.valueOf(contacyCount), icon.getBitmap().getWidth() - 15, 20, countPaint);
        return contactIcon;
    }

}

