package com.hisense.app;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import com.hisense.pos.aidl.hk716Api;
import com.hisense.utils.GlobalData;



/**
 * Created by ganjing on 2019/2/18.
 */

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static Context mContext;
    private static MyApplication instance;

    /**
     * 绑定服务
     */
    ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GlobalData.hk716api = hk716Api.Stub.asInterface(service);
            Log.i(TAG, "onServiceConnected: ");
            try {
                GlobalData.printer = GlobalData.hk716api.getPrinter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ");
            GlobalData.hk716api = null;
            GlobalData.printer = null;
        }
    };

    private boolean BindService() {
        Intent intent = new Intent();
        Log.e("MyApplication", "BindService");
        intent.setPackage("com.hisense.hk716api");
        return bindService(intent, serviceConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        BindService();
        //requestWriteSettings();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(serviceConn);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 申请权限
     */
    public static void requestWriteSettings() {
        int systemBrightness = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "SDK_INT >= Build.VERSION_CODES.M" );
            //大于等于23 请求权限
            if (!Settings.System.canWrite(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        } else {

        }
    }

    /**
     *      * 增加系统亮度
     *      * @return
     */
    public static int addBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "systemBrightness = " + systemBrightness);
            systemBrightness += 10;
            if (systemBrightness >= 255)
                systemBrightness = 255;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, systemBrightness);
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "systemBrightness = " + systemBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }
    /**
     *      * 降低系统亮度
     *      * @return
     */
    public static int subtractBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "systemBrightness = " + systemBrightness);
            systemBrightness -= 10;
            if (systemBrightness <= 0)
                systemBrightness = 0;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, systemBrightness);
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "systemBrightness = " + systemBrightness);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }
}
