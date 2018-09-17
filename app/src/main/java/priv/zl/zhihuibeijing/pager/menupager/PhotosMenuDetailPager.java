package priv.zl.zhihuibeijing.pager.menupager;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import priv.zl.zhihuibeijing.R;
import priv.zl.zhihuibeijing.common.AppNetConfig;
import priv.zl.zhihuibeijing.domain.PhotosBean;
import priv.zl.zhihuibeijing.utils.CacheUtils;

public class PhotosMenuDetailPager extends BaseMenuDetailPager {


    ListView lvPhoto;
    GridView gvPhoto;
    AsyncHttpClient client;
    private ArrayList<PhotosBean.PhotoNews> news;

    ImageButton btnPhoto;

    private boolean isListView = true;

    public PhotosMenuDetailPager(Activity activity, ImageButton imageButton) {
        super(activity);
        btnPhoto = imageButton;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListView) {
                    // 切成gridview
                    lvPhoto.setVisibility(View.GONE);
                    gvPhoto.setVisibility(View.VISIBLE);
                    btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

                    isListView = false;
                } else {
                    // 切成listview
                    lvPhoto.setVisibility(View.VISIBLE);
                    gvPhoto.setVisibility(View.GONE);
                    btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);

                    isListView = true;
                }
            }
        });
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_photo_menu_detail, null);
        lvPhoto = view.findViewById(R.id.lv_photo);
        gvPhoto = view.findViewById(R.id.gv_photo);

        View viewById = view.findViewById(R.id.lv_photo);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        String cache = CacheUtils.getCache(AppNetConfig.BASEURLJSON + "photos/photos_1.json");


        if (!TextUtils.isEmpty(cache)) {
            processData(cache);


        }
        client = new AsyncHttpClient();
        System.out.println("网址：" + AppNetConfig.BASEURLJSON + "photos/photos_1.json");
        client.get(AppNetConfig.BASEURLJSON + "photos/photos_1.json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                processData(content);
                CacheUtils.setCache(AppNetConfig.BASEURLJSON + "photos/photos_1.json", content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                System.out.println("获取网络数据出错" + AppNetConfig.BASEURLJSON + "photos/photos_1.json");
            }
        });

    }

    private void processData(String result) {
        System.out.println("获取到图片信息：" + result);
        Gson gson = new Gson();
        PhotosBean photoBean = gson.fromJson(result, PhotosBean.class);
        news = photoBean.data.news;

        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());


    }

    private class PhotoAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                ViewHolder holder = new ViewHolder();

                convertView = View.inflate(mActivity, R.layout.item_list_photes, null);
                holder.ivPic = convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            // Picasso.get().load(AppNetConfig.BASEURL + getItem(position).listimage).into(holder.ivPic);
            MyBitmapUtils.get().display(holder.ivPic, AppNetConfig.BASEURL + getItem(position).listimage);
            holder.tvTitle.setText(getItem(position).title);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
    }


}
