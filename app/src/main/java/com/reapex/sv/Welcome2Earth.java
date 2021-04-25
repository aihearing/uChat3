package com.reapex.sv;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

public class Welcome2Earth extends AppCompatActivity{

    final static String TAG = Welcome2Earth.class.getSimpleName();

    AlertDialog dialog;

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

        if(!MySP.getInstance().getDisclaimer().equals("AGREE")){
            disclaimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) { dialog.dismiss(); dialog = null;}
        if(!MySP.getInstance().getDisclaimer().equals("AGREE")){
            disclaimer();
        }
    }

    /**
     * Dispatch onStop() to all fragments.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (null != dialog)        dialog.dismiss();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
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

    private void disclaimer(){

        dialog = new AlertDialog.Builder(Welcome2Earth.this)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.disclaimer_content)
                .setTitle(getString(R.string.disclaimer_title))
                .setCancelable(false)
                .setNeutralButton(this.getString(R.string.disclaimer_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Welcome2Earth.this, MyWeb.class);
                        intent.putExtra("from", "agreement");
                        startActivity(intent);
                    }
                })
                .setPositiveButton(this.getString(R.string.agree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySP.getInstance().setDisclaimer("AGREE");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(this.getString(R.string.disagree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Welcome2Earth.this.finish();
                    }
                }).create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.LTGRAY);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.LTGRAY);
    }
}