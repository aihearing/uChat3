package com.reapex.sv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.reapex.sv.db.AChatDB;
import com.reapex.sv.db.AUser;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author  LeoReny@hypech.com
 * @version 1.0
 * @since   2021-04-16
 */

public class Login extends BaseActivity implements View.OnClickListener {
    final String TAG = "000000";  //"this.getClass().getSimpleName()";
    private View mLayout;

    EditText mEditTextPhone, mEditTextPassword;
    String   mPassword,      mPhone;
    AChatDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.hideSystemUI(this);
        Log.e(TAG, "here 1");

        setContentView(R.layout.a_login);
        mLayout = findViewById(R.id.a_login);

        findViewById(R.id.button_ok).setOnClickListener(this);

        mEditTextPhone    = findViewById(R.id.edit_text_phone);
        mEditTextPassword = findViewById(R.id.edit_text_password);
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "here 1");
        mPhone    = mEditTextPhone.getText().toString();
        mPassword = mEditTextPassword.getText().toString();
        if (TextUtils.isEmpty(mPhone)) {
            Snackbar sb = Snackbar.make(mLayout, getString(R.string.no_space), Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(Color.WHITE);
            sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.button_ok));
            sb.show();
        }else if (TextUtils.isEmpty(mPassword)) {
            Snackbar sb = Snackbar.make(mLayout, getString(R.string.no_space), Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(Color.WHITE);
            sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.button_ok));
            sb.show();
        }else if(!MyUtil.isValidChinesePhone(mPhone)) {
            Snackbar sb = Snackbar.make(mLayout, getString(R.string.phone_wrong), Snackbar.LENGTH_SHORT);
            sb.getView().setBackgroundColor(Color.WHITE);
            sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.button_ok));
            sb.show();
        }else{
            Log.e(TAG, "here 1");
            new Thread(() -> {
                db = AChatDB.getDatabase(Login.this);
                List<AUser> list = db.getUserDao().getUserAndPassword(mPhone, mPassword);
                if (list.size() >= 1) {
                    MySP.getInstance().setLogin(true);
                    AUser user = list.get(0);
                    MySP.getInstance().setUPhone(user.getUserPhone());
                    MySP.getInstance().setUNickName(user.getUserNickName());
                    MySP.getInstance().setUPassword(user.getUserPassword());
                    MySP.getInstance().setUAvatar(user.getUserAvatarUri());
                    MySP.getInstance().setUWxId(user.getUserWxId());
                    startActivity(new Intent(Login.this, Welcome3Main.class));
                } else {
                    Snackbar sb = Snackbar.make(mLayout, getString(R.string.phone_not_register), Snackbar.LENGTH_SHORT);
                    sb.getView().setBackgroundColor(Color.WHITE);
                    sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.button_ok));
                    sb.show();
                }
            }).start();
        }
    }

    public void back(View view) {
        Log.e(TAG, "here 1");
        finish();
    }

}