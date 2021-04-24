package com.reapex.sv.frag3me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.MySP;
import com.reapex.sv.Welcome1;

public class Setting extends BaseActivity implements View.OnClickListener {
    final String TAG = this.getClass().getSimpleName();

    RelativeLayout mAboutRl, mLogOutRl, mAccountSecurityRl;
    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mTitleTv = findViewById(R.id.text_view_title);

        mAccountSecurityRl = findViewById(R.id.rl_account_security);
        mAboutRl = findViewById(R.id.rl_about);
        mLogOutRl = findViewById(R.id.rl_log_out);

        mAccountSecurityRl.setOnClickListener(this);
        mTitleTv.setOnClickListener(this);
        mAboutRl.setOnClickListener(this);
        mLogOutRl.setOnClickListener(this);

        MySP.getInstance().init(this);
        initView();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_account_security) {
            startActivity(new Intent(Setting.this, AccountSecurity.class));
        } else if (view.getId() == R.id.rl_about) {
            startActivity(new Intent(Setting.this, About.class));
        } else if (view.getId() == R.id.rl_log_out) {
            // 清除sharedpreferences中存储信息
            MySP.getInstance().setLogin(false);
//            MySP.getInstance().setUser(null);

            Log.e(TAG, "here");
            // 跳转至登录页
            Intent intent = new Intent(this, Welcome1.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //关键的一句，将新的activity置为栈顶
            startActivity(intent);
//            finish();
        }
    }
}
