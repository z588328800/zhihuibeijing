package priv.zl.zhihuibeijing.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mActivity, theFragmentLayoutId(), null);
        initView(view);
        return view;
    }

    /**
     * 初始化View，可以对参数使用view.findViewById方法
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     *
     * @return 返回当前FragmentId
     */
    public abstract int theFragmentLayoutId();

    //在activity的onCreat方法执行完后执行，一般用于初始化数据
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract void initData();
}
