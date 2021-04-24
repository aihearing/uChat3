package com.reapex.sv.frag3me;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.reapex.sv.MySP;
import com.reapex.sv.R;
import com.reapex.sv.Register;
import com.reapex.sv.Welcome2Earth;
import com.reapex.sv.db.AChatDB;
import com.reapex.sv.db.AUser;
import com.reapex.sv.MyUtil;

public class CloseAccount extends AppCompatActivity {
    static final String TAG = Register.class.getSimpleName();
    String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_close_account);
        View mLayout = findViewById(R.id.a_close_accout);
        EditText mEditTextPhone = findViewById(R.id.edit_text_phone);

        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mEditTextPhone.getText().toString();
                if (!MyUtil.isValidChinesePhone(phone)) {
                    Snackbar sb = Snackbar.make(mLayout, getString(R.string.phone_wrong), Snackbar.LENGTH_SHORT);
                    sb.getView().setBackgroundColor(ContextCompat.getColor(CloseAccount.this, R.color.design_default_color_error));
                    sb.setAnchorView(findViewById(R.id.view_1));
                    sb.show();
                } else {
                    showDialog();
                }
            }
        });
    }

    public void back(View view) {        finish();    }

    private void showDialog() {
        Dialog dialog=new AlertDialog.Builder(this)
                .setTitle("我的账号")//设置标题
                .setMessage("关闭账号将无法恢复。")//设置提示内容
                //取消按钮
                .setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                //确定按钮
                .setPositiveButton("确定关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AChatDB db = AChatDB.getDatabase(CloseAccount.this);
                                db.getUserDao().updatePassword(phone,"aksdjfDSFERDf343df34");
                                AUser user = new AUser();
                                user.setUserPhone(phone);
                                db.getUserDao().delete(user);

                        MySP.getInstance().setLogin(false);
                        MySP.getInstance().setUPhone("999");

                        Intent intent = new Intent(CloseAccount.this, Welcome2Earth.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .create();//创建对话框
        dialog.show();//显示对话框
        ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.LTGRAY);
        ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.register_btn_bg_enable));

    }
}