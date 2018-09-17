package priv.zl.zhihuibeijing.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import priv.zl.zhihuibeijing.fragment.ContentFragment;
import priv.zl.zhihuibeijing.fragment.LeftFragment;
import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.utils.UIUtils;

/**
 * 引入库
 * 继承SlidingFragmentActivity
 * onCreate方法改成public
 * 调用相关api
 */

public class MainActivity extends SlidingFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置menu布局文件
        setBehindContentView(R.layout.left_menu);

        //获得slidingMenu
        SlidingMenu slidingMenu = getSlidingMenu();

        //设置全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        //设置侧边栏拉开后屏幕的预留宽度，单位是像素点，为了保证不同分辨率的屏幕显示的效果相同，则设置占用屏幕的2/3
        slidingMenu.setBehindOffset(UIUtils.getDisplayWidthPx() * 2 / 3);

        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //创建Fragment，并替换原来的FrameLayout布局
        ft.replace(R.id.fm_left, new LeftFragment(), "left");
        ft.replace(R.id.fm_content, new ContentFragment(), "main");
        ft.commit();
        Fragment left = fm.findFragmentByTag("left");

    }

    /**
     * 获取侧边栏Fragment对象，从而可以让Activity和Fragment联系起来
     *
     * @return Fragment
     */
    public Fragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment left = fm.findFragmentByTag("left"); //根据tab找到对应的fragment
        return left;
    }

    /**
     * 获取ContentFragment对象
     *
     * @return Fragment
     */
    public Fragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment main = fm.findFragmentByTag("main"); //根据tab找到对应的fragment
        return main;
    }
}
