package com.hisense.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.hisense.app.MyApplication;

/**
 * Created by ganjing on 2019/2/18.
 */

public class AndroidUtils {

    private static AudioManager mAudioManager;

    //获取序列号
    public static String getMachineNo() {
        return android.os.Build.SERIAL;
    }

    //获取手机名称
    public static String getDeviceModel() {
        String phoneName = android.os.Build.MODEL;
        return phoneName;
    }

    //获取手机品牌
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    //获取安卓版本
    public static String getandroidVersion() {
        String androidVersion = Build.VERSION.RELEASE;
        return androidVersion;
    }

    //获取系统版本
    public static String getsystemVersion() {
        String systemVersion = Build.DISPLAY;
        return systemVersion;
    }

    //获取主屏幕分辨率
    public static String getMScreen() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager win = (WindowManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            win.getDefaultDisplay().getRealMetrics(metrics);
        }
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return (width + "×" + height);
    }

    //获取IMEI
    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        if (IMEI == null) {
            return "NULL";
        } else {
            return IMEI;
        }
    }

    //判断手机是否ROOT
    public static String isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        if (root) {
            return "Y";
        } else {
            return "N";
        }
    }

    //判断蓝牙是否开启
    public static String isBTConnected() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        //adapter也有getState(), 可获取ON/OFF...其它状态
        if (blueadapter.isEnabled()) {
            return "true";
        } else {
            return "false";
        }
    }

    //网络状态
    public static String isNetworkConnected() {
        if (MyApplication.getContext().getApplicationContext() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getContext().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() + "";
            }
        }
        return false + "";
    }

    //获取运营商信息
    public static String getOperator() {
        TelephonyManager telManager = (TelephonyManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return "中国移动";
            } else if (operator.equals("46001")) {
                return "中国联通";
            } else if (operator.equals("46003")) {
                return "中国电信";
            }
        }
        return "Disable";
    }

    public static String getFontSize() {
        Configuration mCurConfig = new Configuration();
//        Log.w("ss", "getFontSize(), Font size is " + mCurConfig.fontScale);
        return mCurConfig.fontScale + "";

    }

    //获得音量大小
    public static String getVol() {
        mAudioManager = (AudioManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        //获得系统默认音量，退出时恢复系统音量
        int defaultVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return Integer.toString(defaultVol) + ":" + maxVol;
    }

    //获得屏幕亮度
    public static String getBrightness() {
        int normal = Settings.System.getInt(MyApplication.getContext().getApplicationContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        return Integer.toString(normal);
    }

    //获得休眠时间
    public static String getScreenOffTime() {
        int screenOffTime = 0;
        try {
            screenOffTime = Settings.System.getInt(MyApplication.getContext().getApplicationContext().getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Exception localException) {
        }
        long covert = (long) (screenOffTime / 1000);
        String time = ComnUtils.getTimeFomat(covert);
        return time;
    }

    //获取语言
    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    //获取默认输入法包名
    public static String getDefaultInputMethodPkgName() {
        String mDefaultInputMethodPkg = null;
        String mDefaultInputMethodCls = Settings.Secure.getString(
                MyApplication.getContext().getApplicationContext().getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
        //输入法类名信息
        if (!TextUtils.isEmpty(mDefaultInputMethodCls)) {
            //输入法包名
            mDefaultInputMethodPkg = mDefaultInputMethodCls.split("/")[0];
        }
        return mDefaultInputMethodPkg;
    }

    //获取开发者模式
    public static String getADBStatus() {
        String ADBStatus = Settings.Secure.getString(
                MyApplication.getContext().getApplicationContext().getContentResolver(),
                Settings.Secure.ADB_ENABLED);
        if (ADBStatus.equalsIgnoreCase("1")) {
            return "true";
        }
        return "false";
    }

    // 获取android当前可用内存大小，单位mb
    public static long getAvailRAM(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return mi.availMem;// 将获取的内存大小规格化
    }

    public static long getTotalRAM(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.totalMem;
    }

    //外部存储(SDCard)是否可用
    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    //获取手机外部可用空间大小  ,单位GB
    public static long getAvailableStorge() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long availableBlocks = mStatFs.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            return 1;
        }
    }

    //获取手机外部空间大小 ,单位GB
    public static long getTotalStorge() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs mStatFs = new StatFs(path.getPath());
            long blockSize = mStatFs.getBlockSize();
            long totalBlocks = mStatFs.getBlockCount();
            return blockSize * totalBlocks;
        } else {
            return 1;
        }
    }

    //获取运行时间
    public static String getRunTime() {
        long runTime = SystemClock.elapsedRealtime() / 1000;
        return Long.toString(runTime);
    }

    public static String getSignalStrength() {
        return "0";
    }

    public static String getSysTime() {
        Calendar calendar = Calendar.getInstance(); //获取系统的日期 /
        // /年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //获取系统时间 //小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //分钟
        int minute = calendar.get(Calendar.MINUTE);
        //秒
        int second = calendar.get(Calendar.SECOND);
        return ("日期:" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second);
    }
    //获得电量
    public static String getBatteryCap() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = MyApplication.getContext().getApplicationContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return Integer.toString(level);
    }

    //触摸屏固件版本
    public static String getTouchScreenVersion() {
        //ft5x0x触摸屏 固件版本	读取文件值 /sys/bus/i2c/drivers/ft5x0x_ts/2-0038/ftstpfwver
        String touchScreenVersion_path = "/sys/ctp/ctp_test/fwversion";
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(touchScreenVersion_path);
            br = new BufferedReader(fr);
            String s = br.readLine();
            //Log.d(TAG, "ft5x0x触摸屏 固件版本:"+s);
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "未知";
        } catch (IOException e) {
            e.printStackTrace();
            return "未知";
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //基带版本
    public static String getBasebandVersion() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            //Log.d(TAG, "基带版本: " + result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    //Linux 内核版本
    public static String getKernelVersion() {
        String procVersionStr;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/version"), 256);
            try {
                procVersionStr = reader.readLine();
            } finally {
                reader.close();
            }
            final String PROC_VERSION_REGEX =
                    "\\w+\\s+" + /* ignore: Linux */
                            "\\w+\\s+" + /* ignore: version */
                            "([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
                            "\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /* group 2: (xxxxxx@xxxxx.constant) */
                            "\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
                            "([^\\s]+)\\s+" + /* group 3: #26 */
                            "(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
                            "(.+)"; /* group 4: date */
            Pattern p = Pattern.compile(PROC_VERSION_REGEX);
            Matcher m = p.matcher(procVersionStr);
            if (!m.matches()) {
                return "Unavailable";
            } else if (m.groupCount() < 4) {
                return "Unavailable";
            } else {
                return (new StringBuilder(m.group(1)).append("\n").append(m.group(4))).toString();
            }
        } catch (IOException e) {
            return "Unavailable";
        }
    }

    //获取IP地址
    public static String getIPAddress() {
        NetworkInfo info = ((ConnectivityManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                String hostIp = null;
                try {
                    Enumeration nis = NetworkInterface.getNetworkInterfaces();
                    InetAddress ia = null;
                    while (nis.hasMoreElements()) {
                        NetworkInterface ni = (NetworkInterface) nis.nextElement();
                        Enumeration<InetAddress> ias = ni.getInetAddresses();
                        while (ias.hasMoreElements()) {
                            ia = ias.nextElement();
                            if (ia instanceof Inet6Address) {
                                continue;// skip ipv6
                            }
                            String ip = ia.getHostAddress();
                            if (!"127.0.0.1".equals(ip)) {
                                hostIp = ia.getHostAddress();
                                break;
                            }
                        }
                    }
                } catch (SocketException e) {
                    Log.i("yao", "SocketException");
                    e.printStackTrace();
                }
                return hostIp;
            }
        } else {
            return "Not Available";
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    //将得到的int类型的IP转换为String类型
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    //用反射方法读取熔丝描述信息
    public static String getFuseInfo() {
        String fuse = getFuseDesc() + getFuseValue();
        return fuse;
    }

    //用反射方法读取熔丝描述信息
    public static String getFuseDesc() {
        Method getDesc = null;
        String desc = "";
        try {
            getDesc = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class, String.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "无读取的方法";
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "无读取的类";
        }
        try {
            desc = (String) getDesc.invoke(null, "prop.system.fuse.desc", "000000");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "读取发生异常";
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "读取参数错误";
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "反射发生异常";
        }
        if ((desc == null) || (desc.length() == 0)) {
            return "未设置";
        }
        return desc;
    }

    //用反射方法读取熔丝描述信息
    public static String getFuseValue() {
        Method getValue = null;
        String value = "";
        try {
            getValue = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class, String.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "无读取的方法";
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "无读取的类";
        }
        try {
            value = (String) getValue.invoke(null, "prop.system.fuse.value", "000000");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "读取发生异常";
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "读取参数错误";
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "反射发生异常";
        }
        if ((value == null) || (value.length() == 0)) {
            return "未设置";
        }
        if (value.length() > 10) {
            value = value.substring(0, 10);
        }
        return value;
    }

    //SP版本
    public static String getSPVersion() {
//		String SPVersion;
//		Sys system = new Sys(null);
//		byte[] version = new byte[20];
//		int result = system.Sys_ReadVerInfo("SP",version);
//		//Log.e("CPU名称",getCpuName());
//		if(result == 0)
//		{
//			SPVersion = new String(version);
//			return SPVersion;
//		}
//		else
//		{
        return "NULL";
//		}
    }

    //获取CPU名称
    public static String getCPUInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("model name")) {
                    return str2.split("\\:")[1];
                }
                if (str2.contains("Processor")) {
                    return str2.split("\\:")[1];
                }
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;
    }

    //获取cpu核心数
    public static int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d("ss", "CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Print exception
            Log.d("ss", "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return -1;
        }
    }

    public static String getCPURateDesc() {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0) {
                        firstCPUNum = currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }
        return (int) (rate * 100) + "";
    }
}
