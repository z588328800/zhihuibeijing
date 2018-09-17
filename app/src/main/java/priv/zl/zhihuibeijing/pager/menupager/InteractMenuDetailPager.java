package priv.zl.zhihuibeijing.pager.menupager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情-互动");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

}
