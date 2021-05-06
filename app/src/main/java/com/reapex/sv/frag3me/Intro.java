package com.reapex.sv.frag3me;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reapex.sv.BaseActivity;
import com.reapex.sv.Constant;
import com.reapex.sv.MySP;
import com.reapex.sv.R;

/**
 * @author  LeoReny@hypech.com
 * @version 1.0
 * @since   2021-04-07
 */
public class Intro extends BaseActivity {

    AnimationDrawable animationDrawable;
    ImageView         imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_intro);

        TextView mTitleTv= findViewById(R.id.text_view_title);
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);
        ((ImageView)findViewById(R.id.iv_avatar)).setImageResource(R.mipmap.customerservicefemale);

        imageView = findViewById(R.id.image_view_animation);

        TextView mBelowName= findViewById(R.id.text_view_below_company);
        TextView mBelowQR  = findViewById(R.id.text_view_below_qr_code);

        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();


        String src = getIntent().getStringExtra("from");

        if (src.equals("AccountSecurity")){
            mBelowName.setText(getString(R.string.security_center));
            mBelowQR.setText(getString(R.string.security_center_msg));
        }else if (src.equals("help")){
            mBelowName.setText(getString(R.string.uchat));
            mBelowQR.setText(getString(R.string.scan_qr_code_and_add_friends));
        }else if (src.equals("Frag3MeProfile")){
            mBelowName.setText(getString(R.string.uchat));
            mBelowQR.setText(getString(R.string.scan_qr_code_and_add_friends));
        }else if (src.equals("complain")){
            mBelowName.setText(getString(R.string.complain));
            mBelowQR.setText(getString(R.string.scan_qr_code_and_add_friends));
        }else if (src.equals("upgrade")){
            mBelowName.setText(getString(R.string.uchat));
            mBelowQR.setText(getString(R.string.upgrade));
        }
    }

    public void back(View view) {        finish();    }
}