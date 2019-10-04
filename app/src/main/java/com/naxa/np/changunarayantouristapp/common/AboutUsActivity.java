package com.naxa.np.changunarayantouristapp.common;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.franmontiel.localechanger.LocaleChanger;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.Locale;

public class AboutUsActivity extends BaseActivity {

    WebView web;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setupToolbar(getString(R.string.about_us), false);

        initUI();
    }

    private void initUI() {

        web = findViewById(R.id.webViewAboutUs);

        setupWebView(Constant.getAboutUsDemoContent());
        setupWebView(Constant.getAboutUsDemoContentEnglish());

        setUpWebAboutUsWebContent();

    }

    private void setUpWebAboutUsWebContent() {
        String alias = "en";
        String appLanguage = SharedPreferenceUtils.getInstance(ChangunarayanTouristApp.getInstance()).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null);
        if (!TextUtils.isEmpty(appLanguage)) {
            alias = appLanguage;
        }

        switch (alias) {
            case "nep":
                setupWebView(Constant.getAboutUsDemoContent());
                break;

            case "en":
                setupWebView(Constant.getAboutUsDemoContentEnglish());
                break;

            case "ch":
                setupWebView(Constant.getAboutUsContentChinese());
                break;

            default:
                setupWebView(Constant.getAboutUsDemoContentEnglish());
                break;

        }


    }

    private void setupWebView(String aboutUs) {
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadDataWithBaseURL(null, aboutUs, "text/html", "utf-8", null);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
