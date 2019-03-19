package com.hisense.utils;

/**
 * Created by ganjing on 2019/2/21.
 */

import com.hisense.pos.aidl.Printer;
import com.hisense.pos.aidl.hk716Api;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GlobalData {
    public  static hk716Api hk716api = null;
    public static Printer printer  = null;
    public static ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);//启用4个线程;
}
