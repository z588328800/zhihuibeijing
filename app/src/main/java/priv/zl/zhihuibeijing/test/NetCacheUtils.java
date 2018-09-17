package priv.zl.zhihuibeijing.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mob.tools.network.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class NetCacheUtils {

    private HttpConnection urlConnection;
    private static NetCacheUtils mNetCacheUtils;

    private NetCacheUtils() {
    }

    public static NetCacheUtils get() {

        if (mNetCacheUtils == null) {
            mNetCacheUtils = new NetCacheUtils();
        }
        return mNetCacheUtils;
    }


    /**
     * 网络访问url，并将图片放入imageView
     *
     * @param imageView 图片放入的imageView
     * @param url       地址
     */
    public void getBitmapFromNet(ImageView imageView, String url) {
        new BitmapTask().execute(imageView, url);//启动AsncTask
    }


    // 第一个泛型：表示doInBackground的参数类型，execute()方法的参数传入
    // 第二个泛型：onProgressUpdate的参数类型，用于更新进度
    // 第三个参数：doInBackground的返回类型，onPostExecute方法的参数类型
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView imageView;
        private String url;

        /**
         * 预加载，运行在主线程，执行execute()方法后调用
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 正在加载，运行在子线程（核心方法），执行execute()方法后调用
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];

            imageView.setTag(url); //打标记：将当前的imageView和url绑定在一起

            Bitmap bitmap = download(url);
            publishProgress(10);  //调用这个方法后会执行onProgressUpdate方法


            return bitmap;
        }


        /**
         * 更新进度方法，运行在主线程，执行publishProgress方法后调用，逻辑用语更新进度
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 加载结束，运行在主线程（核心方法），可以直接更新UI
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
//                        如果listView滑动很快，那么一个imageView可能会有多个线程同时控制主线程取设置imageView
//                        他们的执行顺序是哪个先下载完就先加载哪个，由于是多线程，所以先下载的不一定先完成，所以
//                        listView会出现显示的图片不一定是最后加载的图片的情况，所以在执行耗时的网络加载图片之前，
//                        先给imageView打标记，标记的内容为url（因为在执行耗时程序之前给imageView打的标记必定是
//                        最后一个url），在加载结束后，在判断这个imageView的标记是否正确，正确就加载，错误就不加载，
//                        留给其他线程来控制加载

                String url = (String) imageView.getTag();
                if (url.equals(this.url)) { //判断图片绑定的url是否是当前bitmap的url，如果是，说明图片正确
                    //设置图片给imageView
                    imageView.setImageBitmap(result);

                    //写本地缓存
                    LocalCacheUtils.get().setLocalCache(url, result);

                    //写内存缓存
                    MemoryCacheUtilsNew.get().setMemoryCache(url, result);

                }

            }
        }
    }

    /**
     * 根据url下载图片
     *
     * @param url
     * @return
     */
    private Bitmap download(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); //连接超时
            conn.setReadTimeout(5000); //读取超时
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                in = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }
        return bitmap;
    }
}
