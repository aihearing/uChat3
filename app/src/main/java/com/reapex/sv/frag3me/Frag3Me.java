package com.reapex.sv.frag3me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.reapex.sv.MyWeb;
import com.reapex.sv.Welcome2Earth;
import com.reapex.sv.R;
import com.reapex.sv.MySP;
import com.reapex.sv.utils.ActivityStackManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag3Me extends Fragment implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    RelativeLayout rSetting, rMeTop, rUpgrade;
    RelativeLayout rAbout, rLogout, rAccSecurity, rGeneral, rHelp, rPlugin;

    ShapeableImageView imageViewAvatar;
    TextView  mNickNameTv;
    TextView  mWxId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.a_frag3_me, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageViewAvatar = getView().findViewById(R.id.image_view_avatar);
        mNickNameTv = getView().findViewById(R.id.text_view_user_name);
        mWxId       = getView().findViewById(R.id.text_view_wx_id);
        rMeTop      = getView().findViewById(R.id.relative_me_top);

        imageViewAvatar.setImageURI(Uri.parse(MySP.getInstance().getUAvatar()));

        mWxId.setText(getString(R.string.wechat_id, MySP.getInstance().getUWxId()));
        mNickNameTv.setText(MySP.getInstance().getUNickName());

        imageViewAvatar.setOnClickListener(this);
        mNickNameTv.setOnClickListener(this);
        mWxId.setOnClickListener(this);
        rMeTop.setOnClickListener(this);
        //top end

        rAccSecurity = getView().findViewById(R.id.relative_layout_account_security);
        rGeneral   = getView().findViewById(R.id.relative_layout_general);
        rAbout   = getView().findViewById(R.id.relative_layout_about);
        rHelp    = getView().findViewById(R.id.relative_layout_help_feedback);
        rLogout  = getView().findViewById(R.id.relative_layout_logout);
        rUpgrade = getView().findViewById(R.id.relative_layout_upgrade);

        rUpgrade.setOnClickListener(this);
        rHelp.setOnClickListener(this);
        rAbout.setOnClickListener(this);
        rLogout.setOnClickListener(this);
        rAccSecurity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((view.getId()== R.id.relative_me_top) || (view.getId()== R.id.image_view_avatar)){
            startActivity(new Intent(getActivity(), Frag3MeProfile.class));
        }else if(view.getId()== R.id.relative_layout_account_security){
            startActivity(new Intent(getActivity(), AccountSecurity.class));
        }else if(view.getId()== R.id.relative_layout_general){
            startActivity(new Intent(getActivity(), Setting.class));
        }else if(view.getId()== R.id.relative_layout_upgrade){
            Intent intent = new Intent(getActivity(), MyWeb.class);
            intent.putExtra("from", "upgrade");
            startActivity(intent);
        }else if(view.getId()== R.id.relative_layout_about){
            startActivity(new Intent(getActivity(), About.class));
        }else if(view.getId()== R.id.relative_layout_help_feedback){
            Intent intent = new Intent(getActivity(), SVQRCode.class);
            intent.putExtra("from", "help");
            startActivity(intent);
        }else if(view.getId()== R.id.relative_layout_logout){
            MySP.getInstance().setLogin(false);
            MySP.getInstance().setUPhone("999");

            ActivityStackManager.getActivityStackManager().popActivity(getActivity());
            Intent intent = new Intent(getActivity(), Welcome2Earth.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mNickNameTv.setText(MySP.getInstance().getUNickName());
        mWxId.setText(getString(R.string.wechat_id, MySP.getInstance().getUWxId()));
        if (!TextUtils.isEmpty(MySP.getInstance().getUAvatar())) {
            imageViewAvatar.setImageURI(Uri.parse(MySP.getInstance().getUAvatar()));
        }
    }
}
