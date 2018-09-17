package priv.zl.zhihuibeijing.utils;

import android.view.View;
import android.view.ViewGroup;

import priv.zl.zhihuibeijing.application.MyApplication;


/**
 * UI工具类，可以直接复制使用
 */

public class UIUtils {
    /**
     * 通过颜色ID获取颜色资源
     *
     * @param colorId
     * @return #000000等
     */
    public static int getColor(int colorId) {
        return MyApplication.context.getResources().getColor(colorId);
    }


    /**
     * 像素转换
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float desity = MyApplication.context.getResources().getDisplayMetrics().density;
        return (int) (dp * desity + 0.5);
    }

    /**
     * 像素转换
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        float desity = MyApplication.context.getResources().getDisplayMetrics().density;
        return (int) (px / desity + 0.5);
    }

    /**
     * 通过ID获取StringArr资源
     *
     * @param arrId
     * @return
     */
    public static String[] getStringArr(int arrId) {
        return MyApplication.context.getResources().getStringArray(arrId);
    }


    /**
     * 设置控件的宽高
     *
     * @param view    控件
     * @param widthPx 宽（单位像素）
     * @param heighPx 高（单位像素）
     */
    public static void setViewWidthAndHeigh(View view, int widthPx, int heighPx) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(widthPx, heighPx);
        } else {
            layoutParams.width = widthPx;
            layoutParams.height = heighPx;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 获取屏幕宽度（单位像素）
     */
    public static int getDisplayWidthPx() {
        return MyApplication.context.getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 获取屏幕高度（单位像素）
     */
    public static int getDisplayHeighPx() {
        return MyApplication.context.getResources().getDisplayMetrics().heightPixels;
    }


}
