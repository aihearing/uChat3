package com.reapex.sv;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Date:2021/1/23
 * auther:Leo Reny@hypech.com
 */

public class MyWeb extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_webview);

        TextView tvTitle = findViewById(R.id.text_view_title);
        WebView  wv      = findViewById(R.id.WV_pure_course);
        ProgressBar bar  = findViewById(R.id.progress_Bar);

        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);

        String pFrom = getIntent().getStringExtra("from");

        if (pFrom.equals("privacy")) {
            wv.loadUrl(Constant.URL_PRIVACY);
            tvTitle.setText(getString(R.string.privacy_policy));
        }else if (pFrom.equals("agreement")) {
            wv.loadUrl(Constant.URL_USER_AGREEMENT);
            tvTitle.setText(getString(R.string.user_agreement));
        }else if (pFrom.equals("web")) {
            wv.loadUrl(Constant.URL);
            tvTitle.setText(getString(R.string.company_name));
        }else if (pFrom.equals("tutorial")) {
            wv.loadUrl(Constant.URL_TUTORIAL);
            tvTitle.setText(getString(R.string.uchat));
        }else if (pFrom.equals("complain")) {
            wv.loadUrl(Constant.URL_COMPLAIN);
            tvTitle.setText(getString(R.string.complain));
        }else if (pFrom.equals("upgrade")) {
            wv.loadUrl(Constant.URL_UPGRADE);
            tvTitle.setText(getString(R.string.uchat));
        }

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv.canGoBack();
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    bar.setVisibility(View.VISIBLE);
                    bar.setProgress(newProgress);
                }
            }
        });
    }

    public void back(View view) {
        finish();
    }
}