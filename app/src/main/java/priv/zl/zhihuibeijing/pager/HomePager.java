package priv.zl.zhihuibeijing.pager;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.fragment.LeftFragment;

public class HomePager extends BasePager {
    public HomePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        flContent.addView(textView);
        tvTitle.setText("首页");
        LeftFragment leftMenuFragment = (LeftFragment) ((MainActivity) mActivity).getLeftMenuFragment();
        leftMenuFragment.setMenuData(null);
    }
}
