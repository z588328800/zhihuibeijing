package priv.zl.zhihuibeijing.test;

import android.graphics.Bitmap;
import android.util.LruCache;


/**
 * - 默认强引用, 垃圾回收器不会回收
 * - 软引用, 垃圾回收器会考虑回收 SoftReference
 * - 弱引用, 垃圾回收器更会考虑回收 WeakReference
 * - 虚引用, 垃圾回收器最优先回收 PhantomReference
 * <p>
 * android2.3版本以后，垃圾回收机制很容易回收非强引用的引用（即使内存并未被占满），所以使用软引用缓存图片后成功调用的概率很低
 * 这时可以使用LruCache类来存储对象,它可以指定对象所占用的内存大小，如果超过这个值才去回收，并且所回收的对象是使用最少的对象
 */
public class MemoryCacheUtilsNew {

    private LruCache<String, Bitmap> mMemoryCache;

    private static MemoryCacheUtilsNew mMemoryCacheUtils;

    private MemoryCacheUtilsNew() {

        long maxMemory = Runtime.getRuntime().maxMemory();//获取系统分别给APP的内存大小

        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {  //构造函数中设置对象总占用的内存的大小
            //返回每个对象的大小,当给LruCache添加对象时，系统会调用这个方法来增加LruCache的当前所占用的内存的大小，用以判断是否超过最大内存
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getRowBytes() * value.getHeight();// 计算图片大小:每行字节数*高度
                return byteCount;
            }
        };

    }

    public static MemoryCacheUtilsNew get() {
        if (mMemoryCacheUtils == null) {
            mMemoryCacheUtils = new MemoryCacheUtilsNew();
        }
        return mMemoryCacheUtils;
    }

    /**
     * 写缓存
     */
    public void setMemoryCache(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    /**
     * 读缓存
     */
    public Bitmap getMemoryCache(String url) {

        return mMemoryCache.get(url);
    }
}
