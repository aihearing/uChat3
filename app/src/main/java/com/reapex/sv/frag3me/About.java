package com.reapex.sv.frag3me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reapex.sv.BuildConfig;
import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.MyWeb;

/**
 * @author  LeoReny@hypech.com
 * @version 1.0
 * @since   2021-04-07
 */
public class About extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_about);

        TextView    textViewAgreement = findViewById(R.id.text_view_user_agreement);
                    textViewAgreement.setOnClickListener(this);
        TextView    textViewPrivacy = findViewById(R.id.text_view_privacy_policy);
                    textViewPrivacy.setOnClickListener(this);
        TextView    textViewVersionName = findViewById(R.id.version_name);
                    textViewVersionName.setText(getString(R.string.version_name, BuildConfig.VERSION_NAME));

        findViewById(R.id.relative_layout_web).setOnClickListener(this);
        findViewById(R.id.relative_layout_tutorial).setOnClickListener(this);
        findViewById(R.id.relative_layout_complain).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.text_view_user_agreement){
            Intent intent = new Intent(this, MyWeb.class);
            intent.putExtra("from", "agreement");
            startActivity(intent);
        }else if(view.getId()== R.id.text_view_privacy_policy){
            Intent intent = new Intent(this, MyWeb.class);
            intent.putExtra("from", "privacy");
            startActivity(intent);
        }else if(view.getId()== R.id.relative_layout_web){
            Intent intent = new Intent(this, MyWeb.class);
            intent.putExtra("from", "web");
            startActivity(intent);
        }else if(view.getId()== R.id.relative_layout_tutorial){
            Intent intent = new Intent(this, Intro.class);
            intent.putExtra("from", "tutorial");
            startActivity(intent);
        }else if(view.getId()== R.id.relative_layout_complain){
            Intent intent = new Intent(this, SVQRCode.class);
            intent.putExtra("from", "complain");
            startActivity(intent);
        }
    }

    public void back(View view) {        finish();    }
}