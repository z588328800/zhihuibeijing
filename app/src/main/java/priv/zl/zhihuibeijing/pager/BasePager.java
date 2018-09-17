package priv.zl.zhihuibeijing.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpClient;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.activity.MainActivity;

/**
 * 5个标签页的基类
 * 这个类可以获取一个View，这个View包含多个组件（这些组件已经在下面获取到并设置成成员变量），用以构成一个页面，这里用于给ViewPager提供view
 */
public abstract class BasePager {
    // 与此pager关联的activity
    Activity mActivity;

    //
    public ImageButton btnMenu;
    public TextView tvTitle;
    public FrameLayout flContent;

    public View mRootView; //当前页面的布局对象
    public ImageButton btnPhoto;//组图切换按钮

    public BasePager(Activity mActivity) {
        this.mActivity = mActivity;
        mRootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null); //通过inflate已经在view中生成了子控件，以下是找到其中的子控件并赋予引用，方便后面设置
        tvTitle = view.findViewById(R.id.tv_title);
        btnMenu = view.findViewById(R.id.btn_menu);
        btnPhoto = view.findViewById(R.id.btn_photo); //用于切换列表和平铺
        flContent = view.findViewById(R.id.fl_content);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });


        return view;
    }

    public void toggle() {
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu(); //获取侧边栏对象
        slidingMenu.toggle(); //如果当前状态是开，调用这个方法后就关闭；反之亦然
    }

    /**
     * 耗时加载数据，这个方法建议在页面被展现的时候调用
     */
    public abstract void initData();

}