package com.reapex.sv.asrshort;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.db.AMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat_ListView extends BaseActivity implements View.OnClickListener {
    final String TAG = this.getClass().getSimpleName();

    protected     ASRManager         oASRManager;
    private final ASRManagerCallBack oASRCallBack = new ASRManagerCallBack();

    TextView textViewRecording;
    ImageView imageViewRecording;
    AnimationDrawable animationRecording;
    MaterialButton mBtnStart, mBtnStop;

    private EditText editText;

    String pUserId, pUserName;
    int pUserAvaR;

    AMessage        aMsg;
    MyListViewAdapter aAdapter;
    List<AMessage>  aList; // = new ArrayList<AMessage>();
    ListView        listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat);               //a_activity_chat is listview

        pUserId   = getIntent().getStringExtra("from1");
        pUserName = getIntent().getStringExtra("from2");
        pUserAvaR = getIntent().getIntExtra("from3", R.mipmap.bg_me_press); //default bg_me_press

        textViewRecording = findViewById(R.id.text_view_recording);
        imageViewRecording = findViewById(R.id.image_view_voice_recording_anim);

        listView  = findViewById(R.id.list_view_message);
        mBtnStart = findViewById(R.id.button_start);
        mBtnStop  = findViewById(R.id.button_stop);
        TextView mFromUserNameTv = findViewById(R.id.text_view_from_user_name);     //nickname 聊天窗口top
        mFromUserNameTv.setText(pUserName);

        AMessage msg1 = new AMessage("大家说，我听，您看。", "800", "你说", R.mipmap.default_user_avatar, false);
        aList   = new ArrayList<>(Arrays.asList(msg1));
        aAdapter= new MyListViewAdapter(Chat_ListView.this, aList);
        listView.setAdapter(aAdapter);
    }

    private class ASRManagerCallBack implements ASRManager.CallBackInterface {
        @Override
        public void onResults(ArrayList<String> results) {
            if (results != null && results.size() > 0) {
                if (results.size() == 1) {
                    aMsg = new AMessage(results.get(0), pUserId, pUserName, pUserAvaR, true);
                    if (ASRManager.howToLine.equals("onStartingOfSpeech")){ //Log.e(TAG, "howToLine: " + ASRManager.howToLine + " means 第一句话");
                        aList.add(aMsg);
                    }else{  //                        Log.e(TAG, "partial2: "  + " howToLine: " + ASRManager.howToLine);
                        aList.set(aList.size() - 1, aMsg);
                    }
                    aAdapter.notifyDataSetChanged();           // refresh ListView when new messages coming
                    listView.setSelection(aList.size());       // go to the end of the ListView
                } else {                          Log.e(TAG, "NEVER COMING" );                }
            }
        }

        @Override
        public void onError(int error) {
            dismissCustomDialog();
            if (error != MLAsrConstants.ERR_SERVICE_UNAVAILABLE) {
                showFailedDialog(getPrompt(error));
            }
        }

        @Override
        public void onFinish() {
            dismissCustomDialog();
            Log.e(TAG, "---------------88 on finishing");
//            stopASR("on6s");
            oASRManager = new ASRManager(Chat_ListView.this, oASRCallBack);    //1第一次RecognizerSV
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start) {
            oASRManager = new ASRManager(this, oASRCallBack);    //1第一次RecognizerSV
            animationRecording = (AnimationDrawable) imageViewRecording.getDrawable();
            animationRecording.start();
            textViewRecording.setText(getString(R.string.tv_click_to_stop));
            mBtnStop.setVisibility(View.VISIBLE);
            mBtnStart.setVisibility(View.INVISIBLE);
        }else if (view.getId() == R.id.button_stop) {
            stopASR("button_stop");

            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            textViewRecording.setText(getString(R.string.asr_start_recording));
            mBtnStop.setVisibility(View.INVISIBLE);
            mBtnStart.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view) {finish();}       // finish() will invoke onPause();

    @Override
    protected void onPause() {              // backend run, stop ASR
        super.onPause();
        stopASR("onPause");
    }

    void stopASR(String src){
        if (oASRManager != null) {
            oASRManager.destroy();
            oASRManager = null;
        }

        if (src.equals("onPause")) {
            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            mBtnStop.setVisibility(View.INVISIBLE);
            mBtnStart.setVisibility(View.VISIBLE);
            textViewRecording.setText(getString(R.string.asr_back));
        }else if (src.equals("button_stop")) {
            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            mBtnStop.setVisibility(View.INVISIBLE);
            mBtnStart.setVisibility(View.VISIBLE);
            textViewRecording.setText(getString(R.string.asr_start_recording));
        }else if (src.equals("on6s")) {
            textViewRecording.setText(getString(R.string.asr_sleeping));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopASR("button_stop");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void dismissCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {            }
        });
    }

    private void showFailedDialog(int res) {
        AlertDialog dialog = new AlertDialog.Builder(Chat_ListView.this)
                .setMessage(res)
                .setPositiveButton(getString(R.string.str_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.btn_blue_normal));
    }

    private int getPrompt(int errorCode) {
        animationRecording.stop();
        switch (errorCode) {
            case MLAsrConstants.ERR_NO_NETWORK:
                return R.string.error_no_network;
            case MLAsrConstants.ERR_NO_UNDERSTAND:
                return R.string.error_no_understand;
            case MLAsrConstants.ERR_SERVICE_UNAVAILABLE:
                return R.string.error_service_unavailable;
            default:
                return errorCode;
        }
    }
}