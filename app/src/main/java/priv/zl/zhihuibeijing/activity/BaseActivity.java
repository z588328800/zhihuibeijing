package priv.zl.zhihuibeijing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import priv.zl.zhihuibeijing.utils.ActivityManager;

public abstract class BaseActivity extends Activity {

    ActivityManager activityManager = ActivityManager.get();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeSetContentView();
        setContentView(theLayoutId());
        activityManager.addActivity(this);
        initView();
    }

    /**
     * 初始化，在此处的代码会在setContentView之前执行
     */
    protected abstract void initBeforeSetContentView();

    /**
     * 这里传入activity的布局id
     *
     * @return
     */
    protected abstract int theLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 跳转activity
     *
     * @param activityClass
     * @param bundle        //可以传入键值对，如果不需要传递信息，这里写null即可
     */
    public void gotoActivity(Class activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 获取传递过来的的键值对，如果没有则返回null
     *
     * @return Bundle
     */
    public Bundle getIntentBundle() {
        return getIntent().getBundleExtra("bundle");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void finish() {
        activityManager.removeActivity(this);
        super.finish();
    }
}
