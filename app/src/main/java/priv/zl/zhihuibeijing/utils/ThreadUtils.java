package priv.zl.zhihuibeijing.utils;

import android.os.Handler;

import priv.zl.zhihuibeijing.application.MyApplication;

public class ThreadUtils {
    /**
     * 保证runnable对象的run方法是运行在主线程当中
     *
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isInMainThread()) { //是否在主线程中运行
            runnable.run();
        } else {
            getHandler().post(runnable);  //将runnable分发到主线程中运行
        }
    }

    /**
     * 判断是否在主线程中运行
     *
     * @return
     */

    private static boolean isInMainThread() {
        //当前线程的id
        int tid = android.os.Process.myTid(); //返回当前线程ID
        if (tid == MyApplication.mainThreadId) {
            return true;
        }
        return false;
    }

    /**
     * 获取主线程handler
     *
     * @return
     */
    public static Handler getHandler() {
        return MyApplication.handler;
    }
}
