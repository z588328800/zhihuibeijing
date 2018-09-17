package priv.zl.zhihuibeijing.pager.tabdetailpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.logging.Handler;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.activity.WebViewActivity;
import priv.zl.zhihuibeijing.common.AppNetConfig;
import priv.zl.zhihuibeijing.domain.NewsMenu;
import priv.zl.zhihuibeijing.domain.NewsTabBean;
import priv.zl.zhihuibeijing.pager.menupager.BaseMenuDetailPager;
import priv.zl.zhihuibeijing.utils.CacheUtils;
import priv.zl.zhihuibeijing.utils.PrefUtils;
import priv.zl.zhihuibeijing.view.NoScrollViewPager;
import priv.zl.zhihuibeijing.view.PullToRefreshListView;
import priv.zl.zhihuibeijing.view.PullToRefreshListViewFoot;

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsTabData mNewsTabData; //单个页签的网络数据
    View view;
    AsyncHttpClient mClient;
    private String mUrl;
    private NewsTabBean newsTabBean;
    private ArrayList<NewsTabBean.TopNews> topnews;
    private ArrayList<NewsTabBean.NewsData> newss;
    private ViewPager vp_top;
    private TextView tvTitle;
    private CirclePageIndicator indicator;
    private PullToRefreshListViewFoot lvList;
    private String mMoreUrl;
    private MyListAdapter myListAdapter;
    private android.os.Handler mHandler;


    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mNewsTabData = newsTabData;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        lvList = view.findViewById(R.id.lv_list);

        View headView = View.inflate(mActivity, R.layout.head_pager_tab_detail, null);  //listView的头布局控件
        vp_top = headView.findViewById(R.id.vp_top);
        tvTitle = headView.findViewById(R.id.tv_title);
        indicator = headView.findViewById(R.id.c_indicator);

        lvList.addHeaderView(headView); //添加头布局
        lvList.setOnRefreshListener(new PullToRefreshListViewFoot.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mClient.get(mUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        CacheUtils.setCache(mUrl, content);
                        processData(content, false);
                        lvList.onRefreshComplete(true);
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        System.out.println("获取网络数据失败" + mUrl);
                        lvList.onRefreshComplete(false);
                    }
                });
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    //下一页有数据
                    getMoreDataFromServer();
                } else {
                    //没有下一页
                    lvList.onRefreshComplete(true); //隐藏脚布局
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT);

                }

            }
        });
        return view;
    }

    private void getMoreDataFromServer() {
        mClient.get(mMoreUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                processData(content, true);
                System.out.println("mMoreUrl" + mMoreUrl);
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                System.out.println("获取网络数据失败" + mMoreUrl);
                lvList.onRefreshComplete(true);
            }
        });

    }

    @Override
    public void initData() {
        mClient = new AsyncHttpClient();
        mUrl = AppNetConfig.BASEURLJSON + mNewsTabData.getUrl();

        String cache = CacheUtils.getCache(mUrl);
        if (cache != null) {
            processData(cache, false);
        }

        mClient.get(mUrl, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(String content) {
                CacheUtils.setCache(mUrl, content);
                processData(content, false);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                System.out.println("获取网络数据失败" + mUrl);
            }
        });


    }

    private void processData(String content, boolean isMore) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(content, NewsTabBean.class);
        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = AppNetConfig.BASEURLJSON + moreUrl;
        } else {
            mMoreUrl = null;
        }


        if (!isMore) {
            topnews = newsTabBean.data.topnews;
            newss = newsTabBean.data.news;
            if (topnews != null) {
                vp_top.setAdapter(new TopNewsAdapter());
                indicator.setViewPager(vp_top);
                vp_top.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { //设置图片下方的说明
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        tvTitle.setText(topnews.get(position).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                tvTitle.setText(topnews.get(0).title);
                indicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
            }


            /**************************************将viewPager设置为图片轮播**********************************************/
            //创建一个Handler，并且自己给自己放信息，形成内循环
            if (mHandler == null) {
                mHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vp_top.getCurrentItem();
                        currentItem++;
                        if (currentItem > topnews.size() - 1) {
                            currentItem = 0;// 如果已经到了最后一个页面,跳到第一页
                        }
                        vp_top.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0, 3000);// 继续发送延时3秒的消息,形成内循环
                    }
                };

                // 保证启动自动轮播逻辑只执行一次
                mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息

                vp_top.setOnTouchListener(new View.OnTouchListener() {


                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                System.out.println("ACTION_DOWN");
                                // 停止广告自动轮播
                                // 删除handler的所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                // mHandler.post(new Runnable() {
                                //
                                // @Override
                                // public void run() {
                                // //在主线程运行
                                // }
                                // });
                                break;
                            case MotionEvent.ACTION_CANCEL:// 取消事件,
                                // 当按下viewpager后,直接滑动listview,导致抬起事件无法响应,但会走此事件
                                System.out.println("ACTION_CANCEL");
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;
                            case MotionEvent.ACTION_UP:
                                System.out.println("ACTION_UP");
                                // 启动广告
                                mHandler.sendEmptyMessageDelayed(0, 3000);
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });
            }

            /******************************************************************************************/


            myListAdapter = new MyListAdapter();
            lvList.setAdapter(myListAdapter);
            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //记录已经查看的
                    NewsTabBean.NewsData newsData = newss.get(position - 2);
                    String newsId = newsData.id + "";
                    String hadSeen = PrefUtils.getString(mActivity, "hadSeen", "");
                    System.out.println("hadSeen" + hadSeen);
                    if (hadSeen.indexOf(newsId) < 0) {
                        PrefUtils.setString(mActivity, "hadSeen", hadSeen + "," + newsId);
                    }


                    //刷新
                    TextView tvTitle = view.findViewById(R.id.tv_title);
                    tvTitle.setTextColor(mActivity.getResources().getColor(R.color.gray));

                    Intent intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra("url", newsData.url);
                    mActivity.startActivity(intent);
                }
            });


        } else {
            //加载更多
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            newss.addAll(moreNews);//将数据追加在原来的集合中
            myListAdapter.notifyDataSetChanged();
        }
    }

    class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.get().load(AppNetConfig.BASEURL + topnews.get(position).topimage).placeholder(R.drawable.topnews_item_default).into(imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    private class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return newss.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return newss.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            NewsTabBean.NewsData news = newss.get(position);

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.item_listview_tabdetailpager, null);

                viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
                viewHolder.tvDate = convertView.findViewById(R.id.tv_date);

                convertView.setTag(viewHolder);
            }

            viewHolder = (ViewHolder) convertView.getTag();
            Picasso.get().load(AppNetConfig.BASEURL + news.listimage).into(viewHolder.ivIcon);
            viewHolder.tvTitle.setText(news.title);
            viewHolder.tvDate.setText(news.pubdate);
            if (PrefUtils.getString(mActivity, "hadSeen", "").indexOf(news.id + "") > 0) {
                viewHolder.tvTitle.setTextColor(mActivity.getResources().getColor(R.color.gray));
            } else {
                viewHolder.tvTitle.setTextColor(mActivity.getResources().getColor(R.color.dark));
            }
            return convertView;
        }
    }


    static class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
