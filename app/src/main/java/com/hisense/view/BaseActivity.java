package com.hisense.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hisense.app.AppManager;

import java.lang.reflect.Field;

/**
 * Created by ganjing on 2019/2/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResID();
    protected abstract void initViews();
    protected abstract void initData(Bundle savedInstanceState);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getAppManager().addActivity(this);
        int layoutResID = getLayoutResID();
        if (layoutResID > 0) {
            setContentView(getLayoutResID());
        }

        initViews();

        initData(savedInstanceState);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!BuildConfig.LOG_DEBUG) {
//            MobclickAgent.onResume(this);
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (!BuildConfig.LOG_DEBUG) {
//            MobclickAgent.onPause(this);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
    }

    protected void openActivity(Class<?> activity) {
        openActivity(activity, null);
    }

    /**
     * @param activity
     * @param bundle
     */
    protected void openActivity(Class<?> activity, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(getApplicationContext(), activity);
        startActivity(intent);
    }

    /**
     * 不让对话框取消
     *
     * @param dialog
     */
    protected void notCancelMyDialog(DialogInterface dialog) {
        setDialog(dialog, false);
    }

    /**
     * 关闭对话框
     *
     * @param dialog
     */
    protected void cancelMyDialog(DialogInterface dialog) {
        setDialog(dialog, true);
    }

    /**
     * 设置对话框
     *
     * @param dialog
     * @param bool   true 关闭对话框 false 不让取消对话框
     */
    private void setDialog(DialogInterface dialog, boolean bool) {
        try {
            Field field = dialog.getClass().getSuperclass()
                    .getDeclaredField("mShowing");
            field.setAccessible(true);
            // 设置mShowing值，欺骗android系统
            field.set(dialog, bool);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
