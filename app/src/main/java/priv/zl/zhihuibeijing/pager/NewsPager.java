package priv.zl.zhihuibeijing.pager;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import java.util.ArrayList;
import java.util.List;

import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.common.AppNetConfig;
import priv.zl.zhihuibeijing.domain.NewsMenu;
import priv.zl.zhihuibeijing.fragment.LeftFragment;
import priv.zl.zhihuibeijing.pager.menupager.BaseMenuDetailPager;
import priv.zl.zhihuibeijing.pager.menupager.InteractMenuDetailPager;
import priv.zl.zhihuibeijing.pager.menupager.NewsMenuDetailPager;
import priv.zl.zhihuibeijing.pager.menupager.PhotosMenuDetailPager;
import priv.zl.zhihuibeijing.pager.menupager.TopicMenuDetailPager;
import priv.zl.zhihuibeijing.utils.CacheUtils;

public class NewsPager extends BasePager {
    AsyncHttpClient asyncHttpClient;
    List<BaseMenuDetailPager> mMenuDetailPagers;
    NewsMenu newsMenu;


    public NewsPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        tvTitle.setText("新闻");
        asyncHttpClient = new AsyncHttpClient(); //在此初始化AsyncHttpClient，不能在类体重初始化，因为子类继承父类，先执行父类的构造函数，再初始化子类的成员变量
        btnMenu.setVisibility(View.VISIBLE);
        getDataFromServer();
    }

    private void getDataFromServer() {

        String cache = CacheUtils.getCache(AppNetConfig.GetJsonNewsCenter1);
        if (cache != null) {
            System.out.println("获取到缓存的内容：" + cache);
            processData(cache);
        }

        asyncHttpClient.get(AppNetConfig.GetJsonNewsCenter1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                System.out.println("恢复---------------------------" + content);
                CacheUtils.setCache(AppNetConfig.GetJsonNewsCenter1, content);
                processData(content);
            }


            @Override
            public void onFailure(Throwable error, String content) {
                System.out.println("获取失败" + content);
            }
        });

    }

    private void processData(String response) {
        Gson gson = new Gson();
        newsMenu = gson.fromJson(response, NewsMenu.class); //获取的信息转换成bean

        //设置侧边栏内容
        MainActivity mainActivity = (MainActivity) mActivity; //获取到主activity
        LeftFragment leftMenuFragment = (LeftFragment) mainActivity.getLeftMenuFragment();
        //根据获得的信息绘制侧边栏内容
        leftMenuFragment.setMenuData(newsMenu.getData());

        mMenuDetailPagers = new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, newsMenu.getData().get(0).getChildren()));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity, btnPhoto));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        setCurrentDetailPager(0);
    }

    //设置菜单详情页
    public void setCurrentDetailPager(int position) {
        //重新给frameLayout添加内容
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        View view = pager.mRootView;

        //清除之前的旧布局
        flContent.removeAllViews();

        //添加新布局
        flContent.addView(view);

        //初始化页面数据
        pager.initData(); //以后建议initData()这个方法在使用到这个页面时再加载，而不是创建对象时加载

        //更新标题
        tvTitle.setText(newsMenu.getData().get(position).getTitle());
        // 如果是组图页面, 需要显示切换按钮
        if (pager instanceof PhotosMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            // 隐藏切换按钮
            btnPhoto.setVisibility(View.GONE);
        }

    }


}
