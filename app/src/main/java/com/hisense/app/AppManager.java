package com.hisense.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by ganjing on 2019/2/25.
 */

public class AppManager {
    private static AppManager appManager;
    private Stack<Activity> activityStack;

    private AppManager() {
        activityStack = new Stack<Activity>();
    }

    public static AppManager getAppManager() {

        if (appManager == null) {
            appManager = new AppManager();
        }
        return appManager;
    }

    /**
     * 添加activity到栈中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {

        activityStack.add(activity);
    }

    /**
     * 获取当前activity
     *
     * @return
     */
    public Activity getCurrentActivity() {

        return activityStack.lastElement();
    }

    /**
     * 结束当前activity
     */
    public void finishCurrentActivity() {

        finishActivity(getCurrentActivity());
    }

    /**
     * 结束指定的activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {

        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名activity
     *
     * @param cls
     */
    public void finishActivity(Class<Activity> cls) {

        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }

        }

    }

    /**
     * 结束所有activity
     */
    public void finishAllActivities() {

        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 退出系统
     *
     * @param context
     */
    public void exitApp(Context context) {
//		MobclickAgent.onKillProcess(context);
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(context.getPackageName());
        System.exit(0);
    }

    /**
     * 只保留栈底
     */
    public void retainStackBottom() {
//		Activity activity = activityStack.pop();
        while (activityStack.size() > 1) {
            activityStack.pop().finish();
        }
//		activityStack.push(activity);
    }
}
