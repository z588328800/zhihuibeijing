package priv.zl.zhihuibeijing.test;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import priv.zl.zhihuibeijing.utils.MD5Utils;

/**
 * 本地缓存
 *
 */
public class LocalCacheUtils {

    private static final String LOCAL_CACHE_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/zhihuibeijing";

    private static LocalCacheUtils mLocalCacheUtils;


    private LocalCacheUtils() {
        System.out.println("本地缓存的路径为：" + LOCAL_CACHE_PATH);
    }

    public static LocalCacheUtils get() {
        if (mLocalCacheUtils == null) {
            mLocalCacheUtils = new LocalCacheUtils();
        }
        return mLocalCacheUtils;
    }

    // 写本地缓存
    public void setLocalCache(String url, Bitmap bitmap) {
        File dir = new File(LOCAL_CACHE_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();// 创建文件夹
        }


        try {
            String fileName = MD5Utils.toMD5(url);

            File cacheFile = new File(dir, fileName);

            bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(
                    cacheFile));// 参1:图片格式;参2:压缩比例0-100; 参3:输出流
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读本地缓存
    public Bitmap getLocalCache(String url) {
        try {
            File cacheFile = new File(LOCAL_CACHE_PATH, MD5Utils.toMD5(url));

            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        cacheFile));
                MemoryCacheUtilsNew.get().setMemoryCache(url, bitmap);
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
