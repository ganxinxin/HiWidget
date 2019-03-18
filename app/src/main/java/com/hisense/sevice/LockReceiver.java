package com.hisense.sevice;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ganjing on 2019/3/6.
 */

public class LockReceiver extends DeviceAdminReceiver {

    private final static String TAG = LockReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "接收");
        super.onReceive(context, intent);
    }
    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(TAG, "激活");
        super.onEnabled(context, intent);
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i(TAG, "取消");
        super.onDisabled(context, intent);
    }
}
