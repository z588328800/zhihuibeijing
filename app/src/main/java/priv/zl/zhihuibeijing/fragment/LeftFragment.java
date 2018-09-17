package priv.zl.zhihuibeijing.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.NestedScrollingChild;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;
import java.util.zip.Inflater;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.domain.NewsMenu;
import priv.zl.zhihuibeijing.pager.NewsPager;

public class LeftFragment extends BaseFragment {
    ListView lvList;
    List<NewsMenu.NewsMenuData> newsMenuDatas;
    int mposition;
    LeftMenuAdapter adapter;

    @Override
    protected void initView(View view) {
        lvList = view.findViewById(R.id.lv_list);

    }

    @Override
    public int theFragmentLayoutId() {
        return R.layout.fragment_left_menu;
    }

    @Override
    public void initData() {


    }

    /**
     * 设置当前的菜单详情页
     *
     * @param position
     */
    public void setCurrentDetailPager(int position) {
        //获取新闻中心的对象
        MainActivity mainActivity = (MainActivity) mActivity;
        //获取contentFragment对象
        ContentFragment contentFragment = (ContentFragment) mainActivity.getContentFragment();
        //获取新闻中心pager对象
        NewsPager newsPager = contentFragment.getNewsPager();
        //设置菜单详情页
        newsPager.setCurrentDetailPager(position);


    }

    public void toggle() {

        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu(); //获取侧边栏对象
        slidingMenu.toggle(); //如果当前状态是开，调用这个方法后就关闭；反之亦然

    }

    //给侧边栏设置数据
    public void setMenuData(List<NewsMenu.NewsMenuData> newsMenuDatas) {
        mposition = 0;

        //更新页面
        this.newsMenuDatas = newsMenuDatas;

        adapter = new LeftMenuAdapter();
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mposition = position;
                adapter.notifyDataSetChanged();

                toggle();//打开或者关闭侧边栏

                //侧边栏点击后，要修改新闻中心的FrameLayout中的内容
                setCurrentDetailPager(position);

            }
        });
    }


    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsMenuDatas == null ? 0 : newsMenuDatas.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return newsMenuDatas == null ? null : newsMenuDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(parent.getContext(), R.layout.item_left_fragment, null);
            TextView tvList = view.findViewById(R.id.tv_list);
            tvList.setText(getItem(position).getTitle());
            if (position == mposition) {      //设置控件的enable，用于设置seletor的颜色
                tvList.setEnabled(true);
            } else {
                tvList.setEnabled(false);
            }
            return view;
        }
    }
}
