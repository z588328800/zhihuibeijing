package priv.zl.zhihuibeijing.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import priv.zl.zhihuibeijing.R;

public class GuideActivity extends BaseActivity {

    ViewPager vp;
    CirclePageIndicator indicator;
    Button btn_enter;

    int[] imageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    @Override
    protected void initBeforeSetContentView() {
    }

    @Override
    protected int theLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        vp = findViewById(R.id.vp);
        indicator = findViewById(R.id.circle_barner);
        btn_enter = findViewById(R.id.btn_enter);
        vp.setAdapter(new MyPagerAdapter());
        indicator.setViewPager(vp);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MainActivity.class, null);
                finish();
            }
        });
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageIds.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setImageResource(imageIds[position]);  //设置成Background是为了让图片能填充控件
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //保持图片长宽比例，并铺满控件，会进行一定的裁剪
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


}
