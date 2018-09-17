package priv.zl.zhihuibeijing.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    //事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; //设置为false表示不拦截子控件的事件
    }

    //重写触摸方法，在滑动时什么也不做
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
