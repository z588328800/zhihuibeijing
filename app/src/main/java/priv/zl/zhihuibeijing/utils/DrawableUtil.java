package priv.zl.zhihuibeijing.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Administrator on 2015/12/19.
 */
public class DrawableUtil {

    /**
     * 通过画图的方式画一个矩形
     *
     * @param rgb
     * @param corneradius
     * @return
     */
    public static GradientDrawable getDrawable(int rgb, int corneradius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(rgb);
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE); //设置图形 ，RECTANGLE表示矩形
        gradientDrawable.setCornerRadius(corneradius); //设置角半径
        gradientDrawable.setStroke(UIUtils.dp2px(1), rgb); //设置画笔
        return gradientDrawable;
    }


    /**
     * 给控件添加状态监听
     *
     * @param normalDrawable
     * @param pressDrawable
     * @return
     */
    public static StateListDrawable getSelector(Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        //给当前的颜色选择器添加选中图片指向状态，未选中图片指向状态
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        //设置默认状态
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

}
