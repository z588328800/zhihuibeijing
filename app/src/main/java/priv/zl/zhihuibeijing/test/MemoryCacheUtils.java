package priv.zl.zhihuibeijing.test;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class MemoryCacheUtils {
    //使用SoftReference对bitmap对象进行软引用，让垃圾回收机制可以正常回收
    //SoftReference的泛型设置需要包装的类
    //创建对象：SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
    //获取对象: Bitmap bitmap1 = softReference.get(); 如果这个对象被回收了，将会返回null
    private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
    private static MemoryCacheUtils mMemoryCacheUtils;

    private MemoryCacheUtils() {
    }

    public static MemoryCacheUtils get() {
        if (mMemoryCacheUtils == null) {
            mMemoryCacheUtils = new MemoryCacheUtils();
        }
        return mMemoryCacheUtils;
    }

    /**
     * 写缓存
     */
    public void setMemoryCache(String url, Bitmap bitmap) {
        SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
        mMemoryCache.put(url, softReference);
    }

    /**
     * 读缓存
     */
    public Bitmap getMemoryCache(String url) {
        SoftReference<Bitmap> soft = mMemoryCache.get(url);
        if (soft != null) {
            return soft.get();
        }
        return null;
    }
}
