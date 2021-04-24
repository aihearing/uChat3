package com.reapex.sv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.click.guide.guide_lib.GuideCustomViews;
import com.click.guide.guide_lib.interfaces.CallBack;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.mlsdk.common.MLApplication;

/**
 * 引导页
 */
public class Welcome1 extends BaseActivity implements CallBack {

    final String TAG = this.getClass().getSimpleName();
    static final String API_KEY = "client/api_key";     // huawei

    private GuideCustomViews GuideCustomViews;
    private TextView mNextTv;
    private final int[] mPageImages = {
            R.drawable.welcome1,
            R.drawable.welcome2,
            R.drawable.welcome3,
            R.drawable.welcome4
    };

    private final int[] mGuidePoint = {
            R.drawable.icon_guide_point_select,
            R.drawable.icon_guide_point_unselect
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setApiKey();        // huawei

        MySP.getInstance().init(this);
        if(!MySP.getInstance().getWelcome().equals("")){
            Intent intent = new Intent(this, Welcome2Earth.class);
            startActivity(intent);
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.a_welcome);

        initView();
    }

    private void initView() {
        GuideCustomViews = findViewById(R.id.guide_CustomView);
        GuideCustomViews.setData(mPageImages, mGuidePoint, this);
        mNextTv = findViewById(R.id.next_tv);
    }

    private void setApiKey(){
        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(getApplication());
        MLApplication.getInstance().setApiKey(config.getString(API_KEY));
    }

    @Override
    public void callSlidingPosition(int position) {
        Log.e("callSlidingPosition", "滑动位置 callSlidingPosition " + position);
        if(position == 3){
            mNextTv.setVisibility(View.VISIBLE);
        }else{
            mNextTv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void callSlidingLast() {
        Log.e("callSlidingLast", "滑动到最后一个callSlidingLast");
    }

    @Override
    public void onClickLastListener() {
        Log.e("callSlidingLast", "click the last view");
        MySP.getInstance().setWelcome("ok");
        Intent intent = new Intent(Welcome1.this, Welcome2Earth.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        GuideCustomViews.clear();
    }
}
