package priv.zl.zhihuibeijing.pager;

import android.app.Activity;
import android.widget.TextView;

import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.fragment.LeftFragment;

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        tvTitle.setText("智慧服务");
        flContent.addView(textView);
        LeftFragment leftMenuFragment = (LeftFragment) ((MainActivity) mActivity).getLeftMenuFragment();
        leftMenuFragment.setMenuData(null);
    }
}
