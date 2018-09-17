package priv.zl.zhihuibeijing.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.utils.NetCacheUtils;


/**
 * 加载图片到imageView
 */
public class MyBitmapUtils {


    private static MyBitmapUtils myBitmapUtils;

    private MyBitmapUtils() {
    }

    public static MyBitmapUtils get() {
        if (myBitmapUtils == null) {
            myBitmapUtils = new MyBitmapUtils();
        }
        return myBitmapUtils;
    }


    /**
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView, String url) {
        //设置默认图片
        imageView.setImageResource(R.drawable.news_pic_default);
        //优先从内存中加载图片
        Bitmap bitmapMem = MemoryCacheUtilsNew.get().getMemoryCache(url);
        if (bitmapMem != null) {
            imageView.setImageBitmap(bitmapMem);
            System.out.println("读取内存....");
            return;
        }
        //其次从本地加载图片
        Bitmap bitmap = LocalCacheUtils.get().getLocalCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            System.out.println("读取本地....");
            //设置内存缓存
            MemoryCacheUtils.get().setMemoryCache(url, bitmap);
            return;
        }

        //最后从网络加载图片
        NetCacheUtils.get().getBitmapFromNet(imageView, url);
        System.out.println("网络加载....");

    }


    /**
     * 图片缩放
     * wf.wh必须不能是int
     *
     * @param source
     * @param wf
     * @param hf
     * @return
     */
    public static Bitmap zoom(Bitmap source, float wf, float hf) {
        Matrix matrix = new Matrix();
        float sx = wf / source.getWidth();
        float sy = hf / source.getHeight();
        matrix.postScale(sx, sy);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    /**
     * 头像圆形裁剪
     *
     * @param source
     * @return
     */
    public static Bitmap circleBitMap(Bitmap source) {
        final Paint paint = new Paint();
        //抗锯齿效果
        int width = source.getWidth();
        paint.setAntiAlias(true);
        //指定大小bitmap
        Bitmap target = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        //根据target生成一个画布
        Canvas canvas = new Canvas(target);
        //在画布上画了一个圆
        //参数CX,cy-->确定绘制圆的圆心点
        //半径参数
        //画笔
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        //这句话是关键:
        //分析：我们以一张图片作为画布，在上面画了一个圆-->画图展示-->"这时候,绘制的圆和图片本身就出现了一个圆形的交集图案"
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有哪几种
        //PorterDuff.Mode.SRC_IN 取两层图像交集部门,只显示上层图像,注意这里是指取相交叉的部分,然后显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部门,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //使用设置了setXfermode方案的paint绘制图像
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }



    
}
