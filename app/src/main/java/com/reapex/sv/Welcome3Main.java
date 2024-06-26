package com.reapex.sv;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.reapex.sv.asrlong.ChatVIP;
import com.reapex.sv.db.AChatDB;
import com.reapex.sv.frag3me.Frag3Me;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Welcome3Main extends BaseActivity {
    final String TAG = this.getClass().getSimpleName();
    AChatDB db;

    public static final int REQUEST_CODE_SCAN = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_LOCATION = 2;

    public static boolean isForeground = false;

    //    private Frag2Discover mFrag2;
    private Fragment[]    mFragments;
    private Frag0   mFrag0;
    private Frag1   mFrag1;
    private Frag3Me mFrag3;

    private ImageView[] imageViewsBtn;
    private TextView[] textViewsBtn;

    private int mIndex;
    // 当前fragment的index
    private int mCurrentTabIndex;


    // 首页弹出框
    private PopupWindow mPopupWindow;
    private View mPopupView;

    Window window;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        db = AChatDB.getDatabase(this);
        initView();
    }

    private void initView() {
        mFrag0 = new Frag0();
        mFrag1 = new Frag1();
        mFrag3 = new Frag3Me();

        mFragments = new Fragment[]{mFrag0,mFrag1, mFrag3};

        imageViewsBtn = new ImageView[4];
        imageViewsBtn[0] = findViewById(R.id.iv_chats);
        imageViewsBtn[1] = findViewById(R.id.iv_contacts);
        imageViewsBtn[3] = findViewById(R.id.iv_discover);
        imageViewsBtn[2] = findViewById(R.id.iv_me);
        imageViewsBtn[0].setSelected(true);

        textViewsBtn = new TextView[4];
        textViewsBtn[0] = findViewById(R.id.tv_chats);
        textViewsBtn[1] = findViewById(R.id.tv_contacts);
        textViewsBtn[3] = findViewById(R.id.tv_discover);
        textViewsBtn[2] = findViewById(R.id.tv_me);
        textViewsBtn[0].setTextColor(0xFF45C01A);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_fragment_container, mFrag0)
                .add(R.id.rl_fragment_container, mFrag1)
                .add(R.id.rl_fragment_container, mFrag3)
                .hide(mFrag1).hide(mFrag0).hide(mFrag3)
                .show(mFrag0).commit();
    }

    public void onTabClicked(View view) {
        if (view.getId() == R.id.rl_chat){
            mIndex = 0;
//            StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.wechat_common_bg);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.wechat_common_bg));

            mFrag0.getParentFragmentManager().beginTransaction().detach(mFrag0).commit();
            mFrag0.getParentFragmentManager().beginTransaction().attach(mFrag0).commit();

        }else if(view.getId() == R.id.rl_scenario){
            mIndex = 1;
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.wechat_common_bg));
        }else if(view.getId() == R.id.rl_discover){
//            mIndex = 3;
            Intent intent = new Intent(this , ChatVIP.class);
            this.startActivity(intent);
        }else if(view.getId() == R.id.rl_me){
            mIndex = 2;
//           StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.bottom_text_color_normal);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bottom_text_color_normal));
        }
        Log.e(TAG, "midnex" + mIndex);


//        if (mIndex==3) mIndex=1;
        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.rl_fragment_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }


        imageViewsBtn[mCurrentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imageViewsBtn[mIndex].setSelected(true);
        textViewsBtn[mCurrentTabIndex].setTextColor(getColor(R.color.black_deep));
        textViewsBtn[mIndex].setTextColor(0xFF45C01A);
        mCurrentTabIndex = mIndex;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            mFrag0.getParentFragmentManager().beginTransaction().detach(mFrag0).commit();
            mFrag0.getParentFragmentManager().beginTransaction().attach(mFrag0).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        Log.e(TAG, "it's resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db!=null){
            if(db.isOpen()) {db.close();}
            db = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}