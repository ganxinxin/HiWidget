package com.hisense.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.hisense.app.MyApplication;

/**
 * Created by ganjing on 2019/2/18.
 */

public class ComnUtils {
    public static void runApp(String packageName) {
        PackageInfo pi;
        try {
            pi = MyApplication.getContext().getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = MyApplication.getContext().getApplicationContext().getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().getApplicationContext().startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定目录下文件列表的map
     *
     * @param
     * @param folderPath
     * @return
     */
    public static HashMap getFolderFile(String folderPath) {
        HashMap fileMap = new HashMap();
        if (!TextUtils.isEmpty(folderPath)) {
            try {
                File file = new File(folderPath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        String strFilePath = files[i].getAbsolutePath();
                        String strFileName = strFilePath.substring(strFilePath.lastIndexOf("/") + 1);
                        fileMap.put(strFileName, new File(strFilePath));
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileMap;
    }

    /**
     * 获取指定目录下文件列表的map
     *
     * @param
     * @param folderPath
     * @return
     */
    public static List getFolderFileList(String folderPath) {
        List<String> fileList = new ArrayList<>();
        if (!TextUtils.isEmpty(folderPath)) {
            try {
                File file = new File(folderPath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        fileList.add(files[i].getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileList;
    }

    public static String getAppVersion(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Title:
     * @Description: 通过APK地址获取此APP的包名和版本等信息
     * @param:
     * @return:
     */
    public static String getVersionName(String apkpath) {

        PackageManager pm = MyApplication.getContext().getApplicationContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkpath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //获取安装包名称
            Log.i("Abel_Test", "包名是：" + packageName);
            String version = info.versionName; //获取版本信息
            Log.i("Abel_Tes", "版本信息：" + version);
            return version;
        }
        return null;
    }


    public static String getPackageName(String apkpath) {

        PackageManager pm = MyApplication.getContext().getApplicationContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkpath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //获取安装包名称
            Log.i("Abel_Test", "包名是：" + packageName);
            String version = info.versionName; //获取版本信息
            Log.i("Abel_Tes", "版本信息：" + version);
            return packageName;
        }
        return null;
    }
    //获取字符串中的数字
    public static float getNum(String str) {
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return Float.valueOf(str);
    }
    //每秒调用一次，下载速度
    public static String getNetDownSpeed() {

        long totalDownData = TrafficStats.getTotalRxBytes();
        long downData = TrafficStats.getTotalRxBytes() - totalDownData;

        String re = "";
        if (downData > 1024 * 1024) {
            //re = String .format("%.2f",(float)downData/1024/1024) + "Mbps";
            re = String.format("%.2f", (float) downData / 1024 / 1024) + "";
        } else if (downData > 1024) {
            //re = downData /1024 + "Kbps";
            re = String.format("%.2f", (float) downData / 1024 / 1024) + "";
        } else {
            //re = downData + "bps";
            re = String.format("%.2f", (float) downData / 1024 / 1024) + "";
        }
        return re;
    }

    //每秒调用一次，上传速度
    public static String getNetUpSpeed() {

        long totalUpData = TrafficStats.getTotalTxBytes();
        long upData = TrafficStats.getTotalTxBytes() - totalUpData;

        String re = "";
        if (upData > 1024 * 1024) {
            //re = String .format("%.2f",(float)upData/1024/1024) + "Mbps";
            re = String.format("%.2f", (float) upData / 1024 / 1024) + "";
        } else if (upData > 1024) {
            //re = upData /1024 + "Kbps";
            re = String.format("%.2f", (float) upData / 1024 / 1024) + "";
        } else {
            //re = upData + "bps";
            re = String.format("%.2f", (float) upData / 1024 / 1024) + "";
        }
        return re;
    }
    //根据秒数转化为时分秒  1小时1分钟1秒
    public static String getTimeFomat(Long second) {
        if (second < 10) {
            return second + "秒";
        }
        if (second < 60) {
            return second + "秒";
        }
        if (second < 3600) {
            Long minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return minute + "分" + second + "秒";
                }
                return minute + "分" + second + "秒";
            }
            if (second < 10) {
                return minute + "分" + second + "秒";
            }
            return minute + "分" + second + "秒";
        }
        int hour = (int) (second / 3600);
        int minute = (int) ((second - hour * 3600) / 60);
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return hour + "小时" + minute + "分" + second + "秒";
                }
                return hour + "小时" + minute + "分" + second + "秒";
            }
            if (second < 10) {
                return hour + "小时" + minute + "分" + second + "秒";
            }
            return hour + "小时" + minute + "分" + second + "秒";
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + "小时" + minute + "分" + second + "秒";
            }
            return hour + "小时" + minute + "分" + second + "秒";
        }
        if (second < 10) {
            return hour + "小时" + minute + "分" + second + "秒";
        }
        return hour + "小时" + minute + "分" + second + "秒";
    }

    //根据秒数转化为时分秒   00:00:00
    public static String getTime(int second) {
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + ":" + minute + ":0" + second;
            }
            return "0" + hour + ":" + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + ":" + minute + ":0" + second;
        }
        return hour + ":" + minute + ":" + second;
    }
}
