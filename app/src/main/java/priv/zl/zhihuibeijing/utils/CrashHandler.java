package priv.zl.zhihuibeijing.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

/**
 * 全局异常捕获类，这个类可以直接复制使用
 * 此类需要在MyApplication获取单例并初始化
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler = null;
    private Context mContext;
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;


    private CrashHandler() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static CrashHandler get() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    /**
     * 初始化异常处理器，把异常处理器从系统默认的转换成自定义的
     *
     * @param context
     */
    public void init(Context context) {
        //将CrashHandler作为系统默认异常处理器
        this.mContext = context;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler(); //获取系统默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);//设置异常处理器

    }


    //捕获异常后执行
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e != null) {
            handleException(e);
        } else {
            defaultUncaughtExceptionHandler.uncaughtException(t, e); //交给系统默认异常处理器处理
        }
    }


    /**
     * 自定义异常处理
     */
    private void handleException(Throwable e) {
        collectionException(e);
        try {
            Thread.sleep(3000);
            ActivityManager.get().removeAll();
            android.os.Process.killProcess(android.os.Process.myPid()); //杀掉APP所有进程
            System.exit(0);//关闭虚拟机，释放内存

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 收集异常信息
     */
    private void collectionException(Throwable e) {
        final String deviceInfo = Build.DEVICE + Build.VERSION.SDK_INT + Build.MODEL + Build.PRODUCT; //获取设备信息
        final String errorInfo = e.getMessage();  //获取异常信息
        new Thread() {
            @Override
            public void run() {
                Log.e("err", "deviceInfo---" + deviceInfo + ";  errorInfo---" + errorInfo);
            }
        }.run();
    }
}
