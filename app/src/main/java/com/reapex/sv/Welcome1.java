package com.reapex.sv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.mlsdk.common.MLApplication;

public class Welcome1 extends BaseActivity {

    final String TAG = this.getClass().getSimpleName();
    static final String API_KEY = "client/api_key";     // huawei

    private int currentItem;//current item of view pager
    private int itemLength;//length item of view pager
    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private Button btnSkip, btnNext;
    private ViewPagerAdapter adapter;   //adapter of viewPager

    ImageView image;

    private TextView mNextTv;

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
        setContentView(R.layout.a_welcome1);

        image = findViewById(R.id.background);
        Drawable[] backgrounds = new Drawable[4];
        backgrounds[0] = ContextCompat.getDrawable(this, R.drawable.welcome1);
        backgrounds[1] = ContextCompat.getDrawable(this, R.drawable.welcome2);
        backgrounds[2] = ContextCompat.getDrawable(this, R.drawable.welcome3);
        backgrounds[3] = ContextCompat.getDrawable(this, R.drawable.welcome4);

        statusBar();
        viewPager  = findViewById(R.id.view_pager);
        layoutDots = findViewById(R.id.layoutDots);
        btnNext    = findViewById(R.id.btnNext);
        btnSkip    = findViewById(R.id.btnSkip);
        adapter    = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        itemLength = viewPager.getAdapter().getCount();
        showDots(viewPager.getCurrentItem());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                image.setImageDrawable(backgrounds[position]);
            }

            @Override
            public void onPageSelected(int position) {
                showDots(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (viewPager.getCurrentItem() == itemLength - 1){
                    btnSkip.setVisibility(View.GONE);
                    btnNext.setText(R.string.splash_enter);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText(R.string.next);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySP.getInstance().setWelcome("ok");
                goToHome();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == itemLength - 1) {//got it
                    MySP.getInstance().setWelcome("ok");
                    goToHome();
                } else {//next
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    private void setApiKey(){
        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(getApplication());
        MLApplication.getInstance().setApiKey(config.getString(API_KEY));
    }

    private void showDots(int pageNumber) {
        TextView [] dots = new TextView[itemLength];
        layoutDots.removeAllViews();
        for (int i = 0; i< dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            dots[i].setTextColor(ContextCompat.getColor(this,
                    (i == pageNumber ? R.color.dot_active : R.color.dot_incative)));
            layoutDots.addView(dots[i]);
        }
    }

    private void goToHome() {
        startActivity(new Intent(this, Welcome2Earth.class));
        finish();
    }

    private void statusBar() {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class ViewPagerAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(Welcome1.this).inflate(R.layout.a_welcome, container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
