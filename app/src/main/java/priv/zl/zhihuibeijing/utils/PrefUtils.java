package priv.zl.zhihuibeijing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 */

public class PrefUtils {

    /**
     * 获取初始化配置Boolean
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }


    /**
     * 设置初始化配置Boolean
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.edit().putBoolean(key, value).commit();
    }


    /**
     * 获取初始化配置Boolean
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }


    /**
     * 设置初始化配置Boolean
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.edit().putString(key, value).commit();
    }


    /**
     * 获取初始化配置int
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }


    /**
     * 设置初始化配置int
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean setString(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sp.edit().putInt(key, value).commit();
    }


}
