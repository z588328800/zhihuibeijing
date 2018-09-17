package priv.zl.zhihuibeijing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import priv.zl.zhihuibeijing.R;

public class WebViewActivity extends Activity implements View.OnClickListener {


    ImageButton btnBack;
    ImageButton btnTextsize;
    ImageButton btnShare;
    WebView wvNews;
    ProgressBar pbLoading;

    String mUrl;
    private int mTempWhich;
    private int mCurrenWhich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        btnBack = findViewById(R.id.btn_back);
        btnTextsize = findViewById(R.id.btn_textsize);
        btnShare = findViewById(R.id.btn_share);
        wvNews = findViewById(R.id.wv_news);
        pbLoading = findViewById(R.id.pb_loading);


        btnBack.setOnClickListener(this);
        btnTextsize.setOnClickListener(this);
        btnShare.setOnClickListener(this);


        mUrl = getIntent().getStringExtra("url");
        wvNews.loadUrl(mUrl);


        /***********************************浏览器设置*************************************/

        WebSettings settings = wvNews.getSettings();
        settings.setBuiltInZoomControls(true);  //显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 支持js功能  ，一定要写

        /*********************************************************************************/



        /***********************************页面加载回调函数1*******************************/
        wvNews.setWebViewClient(new WebViewClient() {
            //加载页面时执行的操作
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载网页了");
                pbLoading.setVisibility(View.VISIBLE);
            }

            //页面加载结束执行的操作
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("网页加载结束");
                pbLoading.setVisibility(View.INVISIBLE);
            }

            //在页面上点击其他链接时执行的操作，其中url时所点击的链接的地址，view时本webView
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接" + url);
                view.loadUrl(url); // 在跳转链接时强制在当前webview中加载
                return true;
            }
        });
        /*********************************************************************************/


        /******页面加载回调函数2,主要是处理处理JavaScript的对话框，网站图标，网站title，加载进度等*******************************/
        wvNews.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("进度:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                System.out.println("网页标题:" + title);
            }
        });
        /********************************************************************************************************************/


    }

    @Override
    public void onClick(View v) {
        System.out.println("按下按钮：" + v.getId());
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog(); //弹窗选择字体大小
                break;
            case R.id.btn_share:
                showShare();
                break;

        }
    }


    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(items, mCurrenWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wvNews.getSettings();
                switch (mTempWhich) {
                    case 0:
                        // 超大字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        // settings.setTextZoom(22);  不支持太旧的版本
                        break;
                    case 1:
                        //大字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        //正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        //小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        //超小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrenWhich = mTempWhich;

            }
        });

        builder.setNegativeButton("取消", null).show();

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("钟梁的分享内容");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }
}
