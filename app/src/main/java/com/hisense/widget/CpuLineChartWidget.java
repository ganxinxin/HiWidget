package com.hisense.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.RemoteViews;

import com.hisense.R;
import com.hisense.view.LineChart;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by leo on 2019/2/21 0021.
 */

public class CpuLineChartWidget extends AppWidgetProvider {
    // 更新广播动作
    private static final String ACTION_UPDATE_TIME = "action_update_time";

    // 自定义view控件
    private static LineChart lineChart;

    // 绘制的Bitmap
    private static Bitmap bitmap;
    private static Canvas canvas;
    private static AttributeSet attrs;

    // 定时线程池
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private static Set<Integer> widgetIds = new HashSet<>();
    private static Paint paint;
    private static PorterDuffXfermode clear;
    private static PorterDuffXfermode src;

    //x与y坐标轴的长度
    private static int xLength = 990;
    private static int yLength = 432;

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (lineChart == null) {
            // 初始化窗口小部件的内部数据
            lineChart = new LineChart(context, attrs);
            bitmap = Bitmap.createBitmap(xLength, yLength, Bitmap.Config.ARGB_4444);
            canvas = new Canvas(bitmap);
            paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
            src = new PorterDuffXfermode(PorterDuff.Mode.SRC);
            // 开始定时发送更新广播
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ACTION_UPDATE_TIME);
                    context.sendBroadcast(intent);
                }
            }, 0, 1000L, TimeUnit.MILLISECONDS);
        }

        // 绘制最新的折线图片到Bitmap
        paint.setXfermode(clear);
        canvas.drawPaint(paint);
        paint.setXfermode(src);
        lineChart.draw(canvas);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cpu_line_widget);
        // 更新远程ImageView的图片
        views.setBitmap(R.id.line_view, "setImageBitmap", bitmap);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            widgetIds.add(appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // 接收到更新广播，开始更新操作
        if (intent.getAction().equalsIgnoreCase(ACTION_UPDATE_TIME)) {
            int[] widgets = new int[widgetIds.size()];
            int index = 0;
            for (Integer widgetId : widgetIds) {
                widgets[index++] = widgetId;
            }
            onUpdate(context, AppWidgetManager.getInstance(context), widgets);
        }
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
