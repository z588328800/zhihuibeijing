package priv.zl.zhihuibeijing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;

import priv.zl.zhihuibeijing.R;

public class PullToRefreshListViewFoot extends ListView {


    private static final float SPEED = 0.5f;

    /**
     * 状态：需要继续下拉才能刷新
     */
    private static final int STATE_PULL_TO_REFRESH = 1;

    /**
     * 状态：已经下拉到一定值，放手就可以刷新
     */
    private static final int STATE_RELEASE_TO_REFRESH = 2;

    /**
     * //状态：刷新中
     */
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;

    private View mHeadView;
    private int startY = -1;
    private int mHeaderViewHeight;
    private int mHeaderViewWidth;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private RotateAnimation animationUp;
    private RotateAnimation animationDown;
    private ProgressBar progressBar;
    private View mFooterView;
    private int mFooterViewHeight;
    private ProgressBar pbFoot;
    private TextView tvTitleFoot;

    public PullToRefreshListViewFoot(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListViewFoot(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListViewFoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private void initHeaderView() {
        //添加头布局
        mHeadView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        this.addHeaderView(mHeadView);

        tvTitle = mHeadView.findViewById(R.id.tv_title);
        tvTime = mHeadView.findViewById(R.id.tv_time);
        ivArrow = mHeadView.findViewById(R.id.iv_arrow);
        progressBar = mHeadView.findViewById(R.id.pb_loading);
        Glide.with(getContext()).load(R.drawable.giftest).into(ivArrow);  //设置gif动画


        //隐藏头布局
        mHeadView.measure(0, 0);
        mHeaderViewHeight = mHeadView.getMeasuredHeight();
        mHeaderViewWidth = mHeadView.getMeasuredWidth();
        mHeadView.setPadding(0, -mHeaderViewHeight, 0, 0);

        initAnim();//初始化动画
        setCurrentTime();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {  //如果手指开始没有按在控件上，而是 从其他地方滑入的，那么这个控件没有ACTION_DOWN事件，这时startY=-1，那么就从开始滑入的位置记录
                    startY = (int) ev.getY();
                }
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                int endY = (int) ev.getY();
                int dy = endY - startY; //dy是手指滑动的y轴偏移量，初始是0，往下拉则加大
                int firstVisiblePosition = getFirstVisiblePosition();//当前在屏幕顶端的是listview的第几项
                //下拉，并且当前显示的是第一个item
                if (dy > 0 && firstVisiblePosition == 0) {  //如果下拉，并且当前在屏幕顶端的是ListView的第0项，这样判断是为了让当ListView上拉后再下拉时不会进行下拉刷新
                    int padding = (int) (dy * 0.5f - mHeaderViewHeight); //计算当前下拉控件的padding值  dy/2是为了减缓下拉速度

                    mHeadView.setPadding(0, padding, 0, 0);

                    onPull((int) (dy * 0.5), mCurrentState); //拉伸时调用

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) { //如果已经下拉到一定值（padding > 0表示放手就可以刷新了），并且状态不是松开刷新
                        //改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) { //如果没有下拉足够的位置（padding < 0 ），并且状态不是下拉刷新
                        //改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true; //true表示在这里已经消化了onTouch方法
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    //完整展示头布局
                    mHeadView.setPadding(0, 0, 0, 0);

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    //隐藏头布局
                    mHeadView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 动画效果初始化，初始化后可以直接给控件添加效果
     */
    private void initAnim() {
        animationUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationUp.setDuration(200);
        animationUp.setFillAfter(true);


        animationDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationDown.setDuration(200);
        animationDown.setFillAfter(true);


    }

    /**
     * 根据当前的状态改变控件状态
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                progressBar.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                progressBar.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                progressBar.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 刷新结束，收起控件
     *
     * @param ifSuccess 是否成功刷新，用于更新刷新时间
     */
    public void onRefreshComplete(boolean ifSuccess) {

        if (!isLoadMore) {


            mHeadView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            refreshState();
            if (ifSuccess) {
                setCurrentTime();
            }
        } else {
            isLoadMore = false;//表示下拉到低端可以继续刷新
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        }

    }

    /**
     * 拉伸时调用
     *
     * @param heigh        刷新控件的高度
     * @param currentState 当前的状态
     */
    private void onPull(int heigh, int currentState) {

        //拉伸过程中实现动画效果
        if (heigh <= 110) { //下拉位置大于110则不缩放
            float scale = (heigh * 0.6f + 10) / ivArrow.getHeight(); //计算获取缩放值
            ivArrow.setScaleX(scale); //缩放
            ivArrow.setScaleY(scale);
            ivArrow.setPadding(heigh / 8, 0, 0, 0);//平移
        }
    }

    OnRefreshListener mOnRefreshListener;

    /**
     * 设置刷新监听器
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * 刷新事件监听器
     */
    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText("上次更新时间：" + time);
    }

    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_foot, null); //获取下布局
        pbFoot = mFooterView.findViewById(R.id.pb_loading_foot);
        tvTitleFoot = mFooterView.findViewById(R.id.tv_title);

        this.addFooterView(mFooterView);//添加下布局

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight(); //测量控件高度

        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0); //设置控件位置：隐藏
        this.setOnScrollListener(listener);//设置监听
    }


    private boolean isLoadMore = false;

    OnScrollListener listener = new OnScrollListener() {

        //滑动状态发生变化
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            if (scrollState == SCROLL_STATE_IDLE) { //空闲状态
                if (getLastVisiblePosition() == getCount() - 1 && !isLoadMore) { //滑到底部了,并且没有在加载中
                    System.out.println("下拉刷新");
                    mFooterView.setPadding(0, 0, 0, 0);
                    setSelection(getCount() - 1); //将ListView显示在最后一个item上，从而加载更多会直接展示出来，无需手动滑动

                    isLoadMore = true;
                    //通知主界面加载下一页数据
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }

                }
            }
        }

        //滑动过程回调
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    };
}
