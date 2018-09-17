package priv.zl.zhihuibeijing.pager.menupager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.domain.NewsMenu;
import priv.zl.zhihuibeijing.pager.tabdetailpager.TabDetailPager;
import priv.zl.zhihuibeijing.view.NoScrollViewPager;

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    ArrayList<NewsMenu.NewsTabData> mTabDatas;  //页签网络数据
    private List<TabDetailPager> mPagers;  //页签页面集合
    ViewPager vp;
    TabPageIndicator indicator;
    ImageButton btn_next;


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        this.mTabDatas = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        indicator = view.findViewById(R.id.indicator);
        vp = view.findViewById(R.id.vp_news_menu_detail);
        btn_next = view.findViewById(R.id.btn_next);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        for (int i = 0; i < mTabDatas.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity, mTabDatas.get(i));
            mPagers.add(tabDetailPager);
        }
        vp.setAdapter(new MyPagerAdapter());
        indicator.setViewPager(vp);
        //设置页面滑动监听
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("当前位置：" + position);
                SlidingMenu slidingMenu = ((MainActivity) mActivity).getSlidingMenu();
                if (position == 0) {
                    //开启侧边栏
                   // slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); //不开启了 ，
                } else {
                    //关闭侧边栏
                    slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vp.getCurrentItem(); //获取当前itemm
                vp.setCurrentItem(currentItem+1); //跳转下一个页面
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabDatas.get(position).getTitle().toString();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            TabDetailPager pager = mPagers.get(position);
            pager.initData();
            View view = pager.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
