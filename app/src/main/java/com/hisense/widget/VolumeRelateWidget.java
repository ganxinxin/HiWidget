package com.hisense.widget;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.hisense.R;
import com.hisense.app.MyApplication;

import com.hisense.view.LockActivity;


/**
 * Implementation of App Widget functionality.
 */
public class VolumeRelateWidget extends AppWidgetProvider {

    private static final String TAG = "VolumeRelateWidget";

    private AudioManager mAudioManager;
    private ComponentName mComponentName;
    private Context mcontext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        this.mcontext = context;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.volume_relate_widget);
        //增加亮度
        Intent lightaddClick = new Intent("com.zwq.lightadd");
        PendingIntent lightaddPending = PendingIntent.getBroadcast(context, 0, lightaddClick, 0);
        views.setOnClickPendingIntent(R.id.lin_lightadd, lightaddPending);
        //降低亮度
        Intent lightsubtractClick = new Intent("com.zwq.lightsubtract");
        PendingIntent lightsubtractPending = PendingIntent.getBroadcast(context, 0, lightsubtractClick, 0);
        views.setOnClickPendingIntent(R.id.lin_lightsubtract, lightsubtractPending);
        //初始化亮度
        MyApplication.requestWriteSettings();
        views.setProgressBar(R.id.lightbar, 255, MyApplication.addBrightness(), false);
        //增加音量
        Intent volumeaddClick = new Intent("com.zwq.volumeadd");
        PendingIntent volumeaddPending = PendingIntent.getBroadcast(context, 0, volumeaddClick, 0);
        views.setOnClickPendingIntent(R.id.lin_voladd, volumeaddPending);
        //降低音量
        Intent volumesubtractClick = new Intent("com.zwq.volumesubtract");
        PendingIntent volumesubtractPending = PendingIntent.getBroadcast(context, 0, volumesubtractClick, 0);
        views.setOnClickPendingIntent(R.id.lin_volsubtract, volumesubtractPending);
        //初始化音量
        views.setProgressBar(R.id.volbar, 100, addAudio(), false);
        //静音
        Intent silenttaskClick = new Intent("com.zwq.silenttask");
        PendingIntent silenttaskPending = PendingIntent.getBroadcast(context, 0, silenttaskClick, 0);
        views.setOnClickPendingIntent(R.id.lin_volsilent, silenttaskPending);
        //锁屏
        Intent locktaskClick = new Intent(context, LockActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, locktaskClick, 0);
        views.setOnClickPendingIntent(R.id.lin_lock, pendingIntent);

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
        RemoteViews updateVolumeRelateWidget = new RemoteViews(context.getPackageName(), R.layout.volume_relate_widget);
        if (action.equals("com.zwq.lightadd")) {
            updateVolumeRelateWidget.setProgressBar(R.id.lightbar, 255, MyApplication.addBrightness(), false);
        } else if (action.equals("com.zwq.lightsubtract")) {
            updateVolumeRelateWidget.setProgressBar(R.id.lightbar, 255, MyApplication.subtractBrightness(), false);
        } else if (action.equals("com.zwq.volumeadd")) {
            updateVolumeRelateWidget.setProgressBar(R.id.volbar, 100, addAudio(), false);
        } else if (action.equals("com.zwq.volumesubtract")) {
            updateVolumeRelateWidget.setProgressBar(R.id.volbar, 100, substractAudio(), false);
        } else if (action.equals("com.zwq.silenttask")) {
            updateVolumeRelateWidget.setProgressBar(R.id.volbar, 100, slientAudio(), false);
        }
        AppWidgetManager awg = AppWidgetManager.getInstance(context);
        awg.updateAppWidget(new ComponentName(context, VolumeRelateWidget.class), updateVolumeRelateWidget);
    }

    private int addAudio() {
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1;
        if (currentVolume < maxVolume) {
            currentVolume++;
            Log.d(TAG, "==================" + currentVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
        }
        Log.d(TAG, "==================" + 100 * currentVolume / maxVolume);
        return 100 * currentVolume / maxVolume;
    }

    private int substractAudio() {
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume > 0) {
            currentVolume--;
            Log.d(TAG, "==================" + currentVolume);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
        }
        Log.d(TAG, "==================" + 100 * currentVolume / maxVolume);
        return 100 * currentVolume / maxVolume;
    }

    private int slientAudio() {
        //获取系统的Audio管理者
        AudioManager mAudioManager = (AudioManager) MyApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);//设置静音
        return 0;
    }

}

