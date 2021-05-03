package com.reapex.sv.asrshort;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConstants;
import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

import static com.reapex.sv.asrshort.ASRManager.total6s;

public class ChatRecyclerView extends BaseActivity{
    final String TAG = this.getClass().getSimpleName();
    private static final String LANGUAGE_ZH = MLSpeechRealTimeTranscriptionConstants.LAN_ZH_CN;
    private static final String LANGUAGE_EN = MLSpeechRealTimeTranscriptionConstants.LAN_EN_US;
    private static final String LANGUAGE_FR = MLSpeechRealTimeTranscriptionConstants.LAN_FR_FR;
    private String mLanguage = LANGUAGE_ZH;

    ArrayList<String> mArrayList;
    private volatile StringBuffer recognizerResult = new StringBuffer();

    private final ClassOnResultInterface oFromInterface = new ClassOnResultInterface();
    ASRManager oASRManagerRecording;         //huawei

    TextView mTextViewRecording, mTextViewReceiving;
    ImageView mImageViewRecording;
    AnimationDrawable mAnimationRecording;
    MaterialButton mBtnStart, mBtnStop;


    //recyclerView
    RecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat_recycler_view);               //a_activity_chat is listview

        mTextViewRecording = findViewById(R.id.text_view_recording);
        mTextViewReceiving.setGravity(Gravity.TOP);
        mImageViewRecording = findViewById(R.id.image_view_voice_recording_anim);

        mRecyclerView = findViewById(R.id.recycler_view_chat);
        mBtnStart = findViewById(R.id.button_start);
        mBtnStop = findViewById(R.id.button_stop);

        // 1. data to populate the RecyclerView with
        mArrayList = new ArrayList<>();
        mArrayList.add("Horse");

        // 2. set up the RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "permission granted.");
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            showExplanation(getString(R.string.request_permission_title), getString(R.string.permission_rationale_record), Manifest.permission.RECORD_AUDIO);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void showExplanation(String title, String message, final String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        });
        builder.create().show();
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Toast.makeText(this, getString(R.string.request_permission_granted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.request_permission_record_audio), Toast.LENGTH_LONG).show();
            finish();
        }
    });

    public void myClick(View view) {
        if (view.getId() == R.id.button_start) {
            oASRManagerRecording = new ASRManager(this, oFromInterface);    //1第一次RecognizerSV

            mAnimationRecording = (AnimationDrawable) mImageViewRecording.getDrawable();
            mAnimationRecording.start();

            mTextViewRecording.setText(getString(R.string.tv_click_to_stop));

            mBtnStop.setVisibility(View.VISIBLE);
            mBtnStart.setVisibility(View.INVISIBLE);
        }else if (view.getId() == R.id.button_stop) {
            stopASR("button_stop");
            if (mAnimationRecording != null && mAnimationRecording.isRunning()) {
                mAnimationRecording.stop();}
            mTextViewRecording.setText(getString(R.string.asr_start_recording));
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
        if (oASRManagerRecording != null) {
            oASRManagerRecording.destroy();
            oASRManagerRecording = null;
        }
        if (mAnimationRecording != null && mAnimationRecording.isRunning()) {
            mAnimationRecording.stop();}
        mBtnStop.setVisibility(View.INVISIBLE);
        mBtnStart.setVisibility(View.VISIBLE);

        if (src.equals("onPause")) {
            mTextViewRecording.setText(getString(R.string.asr_back));
        }else if (src.equals("button_stop")) {
            mTextViewRecording.setText(getString(R.string.asr_start_recording));
        }else if (src.equals("onOneMinute")) {
            mTextViewRecording.setText(getString(R.string.asr_sleeping));
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

    private class ClassOnResultInterface implements ASRManager.OnResultsReadyInterface {
        @Override
        public void onResults(ArrayList<String> results, Boolean partial) {
            if (results != null && results.size() > 0) {
                if (results.size() == 1) {
                    mTextViewReceiving.setText("");
                    mTextViewReceiving.append(results.get(0));
                    if (ASRManager.howToLine.equals("onStartingOfSpeech")){
                        mArrayList.add(results.get(0));
                        mTextViewReceiving.setText("the end");
                    }else{
                        mTextViewReceiving.setText("the beg");

                    }
                    mAdapter.notifyDataSetChanged();                           // refresh ListView when new messages coming
                    mRecyclerView.smoothScrollToPosition(mArrayList.size());   // go to the end of the ListView
                    Log.e(TAG, "line 269, onresults" + total6s);
                } else {
                    Log.e(TAG, "NEVER COMING" );
                }
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
        public void onFinish() {            dismissCustomDialog();        }

        @Override
        public void onOneMinute() {
            Log.e(TAG, "onOneMinute: #" + total6s);
            stopASR("onOneMinute");
        }
    }

    private void dismissCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {            }
        });
    }

    private void showFailedDialog(int res) {
        AlertDialog dialog = new AlertDialog.Builder(ChatRecyclerView.this)
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
        mAnimationRecording.stop();
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