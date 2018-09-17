package priv.zl.zhihuibeijing.pager;

import android.app.Activity;
import android.widget.TextView;

import priv.zl.zhihuibeijing.activity.MainActivity;
import priv.zl.zhihuibeijing.fragment.LeftFragment;

public class SettingPager extends BasePager {
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        tvTitle.setText("设置");

        flContent.addView(textView);
        LeftFragment leftMenuFragment = (LeftFragment) ((MainActivity) mActivity).getLeftMenuFragment();
        leftMenuFragment.setMenuData(null);
    }
}
