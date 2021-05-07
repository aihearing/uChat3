package com.reapex.sv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

public class Welcome2Earth extends AppCompatActivity{

    final static String TAG = Welcome2Earth.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final View view = View.inflate(this, R.layout.a_splash_earth, null);
        MyUtil.hideSystemUI(this);
        setContentView(view);
        super.onCreate(savedInstanceState);
        Log.e(TAG, "启动.");

        // 打开SDK日志开关
        HiAnalyticsTools.enableLog();
        HiAnalyticsInstance instance = HiAnalytics.getInstance(this);

        MySP.getInstance().init(this);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        view.setAnimation(animation);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Dispatch onStop() to all fragments.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (MySP.getInstance().isLogin()) {
            // 已登录，跳至主页面
            startActivity(new Intent(Welcome2Earth.this, Welcome3Main.class));
            finish();
        }
    }

    public void myClick(View view) {
        if (view.getId() == R.id.btn_login) {
            startActivity(new Intent(Welcome2Earth.this, Login.class));
        }else if (view.getId() == R.id.btn_register) {
            startActivity(new Intent(Welcome2Earth.this, Register.class));
        }
    }
}