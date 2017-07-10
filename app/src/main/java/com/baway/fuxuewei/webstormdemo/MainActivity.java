package com.baway.fuxuewei.webstormdemo;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initWeb();



    }


    public void initView(){

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+" bawei");

        webView.setWebChromeClient(new WebChromeClient(){

        });

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("login://") && url.contains("userlogin")){

                    Uri uri = Uri.parse(url);
                    String userName = uri.getQueryParameter("userName");
                    initToast(userName);

                    nativeToJs();

                    return true;

                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });


        webView.addJavascriptInterface(new MyWeb(), "MyWeb");


    }

    public void initWeb(){

        String userName = "zhangsan";
        webView.loadUrl("file:///android_asset/www/index.html?userName="+userName);

    }

    public void initToast(String value){

        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

    }


    class MyWeb{

        @JavascriptInterface
        public void showToast(String value){

            MainActivity.this.initToast(value);

        }

    }

    public void nativeToJs(){

        if (Build.VERSION.SDK_INT < 19){
            webView.loadUrl("javascript:onNativeToJs('from native')");
        }else {
            webView.evaluateJavascript("javascript:onNativeToJs('from native')", null);
        }

    }

}
