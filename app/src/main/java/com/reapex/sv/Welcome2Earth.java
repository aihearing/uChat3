package com.reapex.sv;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;

import java.util.ArrayList;
import java.util.List;

public class Welcome2Earth extends FragmentActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener {

    final static String TAG = Welcome2Earth.class.getSimpleName();
    private static final int PERMISSION_REQUESTS = 1;

    AlertDialog dialog;
    private static final int sleepTime = 500;
    Button mLoginBtn, mRegisterBtn;
    private static final int SHOW_OPERATE_BTN = 0x3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final View view = View.inflate(this, R.layout.a_splash_earth, null);
        MyUtil.hideSystemUI(this);
        setContentView(view);
        super.onCreate(savedInstanceState);
        Log.e(TAG, "启动.");

        // 打开SDK日志开关
        HiAnalyticsTools.enableLog();
        HiAnalyticsInstance instance = HiAnalytics.getInstance(this);

        mLoginBtn = findViewById(R.id.btn_login);
        mRegisterBtn = findViewById(R.id.btn_register);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);

        MySP.getInstance().init(this);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        view.setAnimation(animation);

        if (!this.allPermissionsGranted()) {        //1. 所有的权限都有了么？
            this.getRuntimePermissions();           //2. 有问题，则去获取
        }
        if(!MySP.getInstance().getDisclaimer().equals("AGREE")){
            disclaimer();
        }
    }

    private boolean allPermissionsGranted() {       //1.2 一个一个查
        for (String permission : this.getRequiredPermissions()) {
            if (!Welcome2Earth.isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private String[] getRequiredPermissions() {     //1.1 从manifest中取所有的权限
        try {
            PackageInfo info = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            int i = 1;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            return new String[0];
        }
    }

    private static boolean isPermissionGranted(Context context, String permission) {    //1.3 一个一个对比
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    private void getRuntimePermissions() {              //2.1 没有授权的，装入allNeeded。
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : this.getRequiredPermissions()) {
            if (!Welcome2Earth.isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {          //2.2 一个一个要授权，结果返回3 OnRequestPermission Result.
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), Welcome2Earth.PERMISSION_REQUESTS);
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) { dialog.dismiss(); dialog = null;}
        if(!MySP.getInstance().getDisclaimer().equals("AGREE")){
            disclaimer();
        }
    }

    /**
     * Dispatch onStop() to all fragments.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (null != dialog)        dialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != Welcome2Earth.PERMISSION_REQUESTS) {
            return;
        }
        boolean isNeedShowDiag = false;
        for (int i = 0; i < permissions.length; i++) {
            if ((permissions[i].equals(android.Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    || (permissions[i].equals(android.Manifest.permission.RECORD_AUDIO) && grantResults[i] != PackageManager.PERMISSION_GRANTED)) {
                // If the storage permissions are not authorized, need to pop up an authorization prompt box.
                isNeedShowDiag = true;
            }
        }
        if (isNeedShowDiag && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            AlertDialog dialog = new AlertDialog.Builder(Welcome2Earth.this)
                    .setMessage(this.getString(R.string.permission_rationale))
                    .setPositiveButton(this.getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            // Open the corresponding setting interface according to the package name.
                            intent.setData(Uri.parse("package:" + Welcome2Earth.this.getPackageName()));
                            Welcome2Earth.this.startActivityForResult(intent, 200);
                            Welcome2Earth.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Welcome2Earth.this.finish();
                        }
                    }).create();

            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (!this.allPermissionsGranted()) {
                this.getRuntimePermissions();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (MySP.getInstance().isLogin()) {
            // 已登录，跳至主页面
            startActivity(new Intent(Welcome2Earth.this, Welcome3Main.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            startActivity(new Intent(Welcome2Earth.this, Login.class));
        }else if (view.getId() == R.id.btn_register) {
            startActivity(new Intent(Welcome2Earth.this, Register.class));
        }
    }

/*    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_OPERATE_BTN) {
                mLoginBtn.setVisibility(View.VISIBLE);
                mRegisterBtn.setVisibility(View.VISIBLE);
            }
        }
    };
*/
    private void disclaimer(){

        // Linkify the message
        final SpannableString s = new SpannableString(getString(R.string.disclaimer_content)); // msg should have url to enable clicking
        Linkify.addLinks(s, Linkify.ALL);

        dialog = new AlertDialog.Builder(Welcome2Earth.this)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.disclaimer_content)
                .setTitle(getString(R.string.disclaimer_title))
                .setCancelable(false)
                .setPositiveButton(this.getString(R.string.global_read), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySP.getInstance().setDisclaimer("AGREE");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(this.getString(R.string.disagree), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Welcome2Earth.this.finish();
                    }
                }).create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.LTGRAY);
        // Make the textview clickable. Must be called after show()
        ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}