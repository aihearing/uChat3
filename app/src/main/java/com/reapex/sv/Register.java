package com.reapex.sv;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.reapex.sv.db.AChatDB;
import com.reapex.sv.db.AUser;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import static com.reapex.sv.MyUtil.validatePassword;

public class Register extends BaseActivity implements View.OnClickListener {
    static final String TAG = Register.class.getSimpleName();
    AChatDB db;

    private static final int UPDATE_AVATAR_BY_ALBUM = 2;
    private View mLayout;

    String mImageName, mAvatarUri;

    ImageView mAgreementIv, mAvatarSdv;
    EditText  mNickNameEt, mPhoneEt, mPasswordEt;
    Button    mRegisterBtn;

    boolean mAgree = true;   //是否同意协议

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.hideSystemUI(this);

        setContentView(R.layout.a_register);
        mLayout = findViewById(R.id.a_register);
        db = AChatDB.getDatabase(this);

        mRegisterBtn    = findViewById(R.id.btn_register);

        mAvatarSdv      = findViewById(R.id.iv_avatar);
        mAvatarSdv.setImageResource(R.mipmap.default_user_avatar);

        mAgreementIv    = findViewById(R.id.image_view_agree);
        mNickNameEt     = findViewById(R.id.et_nick_name);
        mPhoneEt        = findViewById(R.id.edit_text_phone);
        mPasswordEt     = findViewById(R.id.edit_text_password);

        TextView mAgreementTv = findViewById(R.id.text_view_agreement);
        TextView mPrivacy     = findViewById(R.id.text_view_privacy);

        mAvatarSdv.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mAgreementIv.setOnClickListener(this);
        mAgreementTv.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);

        mNickNameEt.addTextChangedListener(new TextChange());
        mPhoneEt.addTextChangedListener(new TextChange());
        mPasswordEt.addTextChangedListener(new TextChange());
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            String nickName = mNickNameEt.getText().toString();
            String phone    = mPhoneEt.getText().toString();
            String password = mPasswordEt.getText().toString();
             if(!MyUtil.isValidChinesePhone(phone)) {
                Snackbar sb = Snackbar.make(mLayout, getString(R.string.phone_wrong), Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(Color.WHITE);
                sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.edit_text_password));
                sb.show();
            }else if (!validatePassword(password)) {
                Snackbar sb = Snackbar.make(mLayout, getString(R.string.password_rules), Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(Color.WHITE);
                sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                sb.setTextColor(Color.RED).setAnchorView(findViewById(R.id.btn_register));
                sb.show();
            }else {
                register(nickName, phone, password, mAvatarUri);
            }
        }else if (v.getId() == R.id.image_view_agree){
            if (mAgree) {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_false);
                mAgree = false;
                Snackbar sb = Snackbar.make(mLayout, getString(R.string.global_agree), Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_error));
                sb.getView().findViewById(com.google.android.material.R.id.snackbar_text).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                sb.setTextColor(Color.WHITE).setAnchorView(findViewById(R.id.image_view_agree));
                sb.show();
            } else {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_true);
                mAgree = true;
            }
            checkSubmit();
        }else if (v.getId() == R.id.iv_avatar){
            Intent intent;
            intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, UPDATE_AVATAR_BY_ALBUM);
        }else if (v.getId() == R.id.image_view_agree){
            if (mAgree) {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_false);
                mAgree = false;
            } else {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_true);
                mAgree = true;
            }
            checkSubmit();
        }else if (v.getId() == R.id.text_view_agreement){
            Intent intent = new Intent(this, MyWeb.class);
            intent.putExtra("from", "agreement");
            startActivity(intent);
        }else if (v.getId() == R.id.text_view_privacy){
            Intent intent = new Intent(this, MyWeb.class);
            intent.putExtra("from", "privacy");
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode==UPDATE_AVATAR_BY_ALBUM) {
                if (data != null) {
                    Uri uri = data.getData();
                    mAvatarUri = uri.toString();
                    mAvatarSdv.setImageURI(uri);
                }
            }
        }
    }

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkSubmit();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    /**
     * 注册
     */
    private void register(String nickName, String phone, String password, String avatar) {
        int i1 = 1;
        if (avatar == null || avatar.length() == 0) {
            avatar = (Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + getResources().getResourcePackageName(R.mipmap.default_user_avatar) + "/"
                    + getResources().getResourceTypeName(R.mipmap.default_user_avatar) + "/"
                    + getResources().getResourceEntryName(R.mipmap.default_user_avatar))).toString();
        }

        String u2WxId = Constant.getWxId();

        int i = 1;

        AUser u2 = new AUser(phone, nickName,  password, avatar, u2WxId);
                db.getUserDao().insert(u2);

        Log.e(TAG,  "  avatar: " + u2.getUserAvatarUri());
        // 登录成功后设置user和isLogin至sharedpreferences中

        MySP.getInstance().setLogin(true);

        MySP.getInstance().setUPhone(phone);
        MySP.getInstance().setUNickName(nickName);
        MySP.getInstance().setUPassword(password);
        MySP.getInstance().setUAvatar(avatar);
        MySP.getInstance().setUWxId(u2WxId);

        startActivity(new Intent(Register.this, Welcome3Main.class));
        Snackbar.make(mLayout, "注册成功！", Snackbar.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 表单是否填充完成(昵称,手机号,密码,是否同意协议)
     */
    private void checkSubmit() {
        boolean nickNameHasText = mNickNameEt.getText().toString().length() > 0;
        boolean phoneHasText = mPhoneEt.getText().toString().length() > 0;
        boolean passwordHasText = mPasswordEt.getText().toString().length() > 0;
        if (nickNameHasText && phoneHasText && passwordHasText && mAgree) {
            // 注册按钮可用
            mRegisterBtn.setBackgroundColor(getColor(R.color.register_btn_bg_enable));
            mRegisterBtn.setTextColor(getColor(R.color.register_btn_text_enable));
            mRegisterBtn.setEnabled(true);
        } else {
            // 注册按钮不可用
            mRegisterBtn.setBackgroundColor(getColor(R.color.register_btn_bg_disable));
            mRegisterBtn.setTextColor(getColor(R.color.register_btn_text_disable));
            mRegisterBtn.setEnabled(false);
        }
    }
}