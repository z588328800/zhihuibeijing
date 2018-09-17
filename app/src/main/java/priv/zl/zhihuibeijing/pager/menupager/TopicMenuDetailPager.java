package priv.zl.zhihuibeijing.pager.menupager;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情-专题");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
