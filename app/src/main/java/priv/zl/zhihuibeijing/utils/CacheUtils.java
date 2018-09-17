package priv.zl.zhihuibeijing.utils;

import priv.zl.zhihuibeijing.application.MyApplication;

public class CacheUtils {

    /**
     * 设置缓存
     *
     * @param url
     * @param json
     */
    public static void setCache(String url, String json) {
        PrefUtils.setString(MyApplication.getContext(), url, json);

    }


    /**
     * 获取缓存
     *
     * @param url
     * @return 没有则返回null
     */
    public static String getCache(String url) {
        return PrefUtils.getString(MyApplication.getContext(), url, null);
    }
}
