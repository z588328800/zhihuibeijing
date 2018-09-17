package priv.zl.zhihuibeijing.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.pager.BasePager;
import priv.zl.zhihuibeijing.pager.GovAffairsPager;
import priv.zl.zhihuibeijing.pager.HomePager;
import priv.zl.zhihuibeijing.pager.NewsPager;
import priv.zl.zhihuibeijing.pager.SettingPager;
import priv.zl.zhihuibeijing.pager.SmartServicePager;
import priv.zl.zhihuibeijing.view.NoScrollViewPager;

public class ContentFragment extends BaseFragment {
    NoScrollViewPager vp_one;
    RadioGroup rg_one;
    List<BasePager> pagers = new ArrayList<>();

    @Override
    protected void initView(View view) {
        vp_one = view.findViewById(R.id.vp_one);
        rg_one = view.findViewById(R.id.rg_one);
    }

    /**
     * 获取第二页（也就是新闻中心页面）的对象
     * @return
     */
    public NewsPager getNewsPager() {
        NewsPager newsPager = (NewsPager) pagers.get(1);
        return newsPager;
    }

    @Override
    public int theFragmentLayoutId() {
        return R.layout.fragment_content_main;
    }

    @Override
    public void initData() {
        pagers.add(new HomePager(mActivity)); //使用viewPager对页面进行切换
        pagers.add(new NewsPager(mActivity));
        pagers.add(new SmartServicePager(mActivity));
        pagers.add(new GovAffairsPager(mActivity));
        pagers.add(new SettingPager(mActivity));

        vp_one.setAdapter(new MyContentPageAdapter());
        pagers.get(0).initData();


        //以下为设置viewPager监听，当切换到指定页目才会初始化这个页的数据 ，每次切换都会初始化数据
        //因为所加载的数据都会进入缓存，所以此处不必考虑每次初始化消耗网络资源的问题
        vp_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) { // 页面展现时对数据进行刷新,而且每次展示都会重新刷新
                pagers.get(position).initData();
                if (position == 0 || position == pagers.size() - 1) {
                    // 首页和设置页要禁用侧边栏
                    setSlidingMenuEnable(false);
                } else {
                    // 其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        rg_one.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_one.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        vp_one.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        vp_one.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        vp_one.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        vp_one.setCurrentItem(4, false);
                        break;
                }
            }
        });
    }

    private class MyContentPageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) { //切换时都会执行，所以不能在这里new控件，只能将控件先new出来，放入集合中，在这里调用集合

            BasePager pager = pagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);

        }
    }

    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    protected void setSlidingMenuEnable(boolean enable) {
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
