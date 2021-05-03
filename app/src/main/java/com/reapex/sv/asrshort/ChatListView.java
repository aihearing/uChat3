package com.reapex.sv.asrshort;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import com.huawei.hms.mlsdk.asr.MLAsrConstants;

import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.adapter.ListViewAdapter;
import com.reapex.sv.db.AMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.reapex.sv.asrshort.ASRManager.total6s;

public class ChatListView extends BaseActivity implements View.OnClickListener {
    final String TAG = this.getClass().getSimpleName();

    private final ClassOnResultInterface oFromInterface = new ClassOnResultInterface();
    ASRManager oASRManager;         //huawei

    TextView textViewRecording;
    ImageView imageViewRecording;
    AnimationDrawable animationRecording;
    MaterialButton mBtnStart, mBtnStop;

    String pUserId, pUserName;
    int pUserAvaR;

    AMessage aMsg;
    ListViewAdapter aAdapter;
    List<AMessage> aList; // = new ArrayList<AMessage>();
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat_list_view);               //a_activity_chat is listview
        pUserId = getIntent().getStringExtra("from1");
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
        aList = new ArrayList<>(Arrays.asList(msg1));
        aAdapter = new ListViewAdapter(ChatListView.this, aList);
        listView.setAdapter(aAdapter);

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_start) {
            oASRManager = new ASRManager(this, oFromInterface);    //1第一次RecognizerSV

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

    public void back(View view) {
        Log.e(TAG, Thread.currentThread().getStackTrace()[4].getMethodName()+ " at line: " + Thread.currentThread().getStackTrace()[4].getLineNumber());
        finish();
    }       // finish() will invoke onPause();

    @Override
    protected void onPause() {              // backend run, stop ASR
        Log.e(TAG, Thread.currentThread().getStackTrace()[4].getMethodName()+ " at line: " + Thread.currentThread().getStackTrace()[4].getLineNumber());
        super.onPause();
        stopASR("onPause");
    }

    void stopASR(String src){
        if (oASRManager != null) {            oASRManager.destroy();            oASRManager = null;        }
        if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
        mBtnStop.setVisibility(View.INVISIBLE);
        mBtnStart.setVisibility(View.VISIBLE);

        if (src.equals("onPause")) {
            textViewRecording.setText(getString(R.string.asr_back));
        }else if (src.equals("button_stop")) {
            textViewRecording.setText(getString(R.string.asr_start_recording));
        }else if (src.equals("onOneMinute")) {
            textViewRecording.setText(getString(R.string.asr_sleeping));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, Thread.currentThread().getStackTrace()[4].getMethodName()+ " at line: " + Thread.currentThread().getStackTrace()[4].getLineNumber());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, Thread.currentThread().getStackTrace()[4].getMethodName()+ " at line: " + Thread.currentThread().getStackTrace()[4].getLineNumber());
    }

    private class ClassOnResultInterface implements ASRManager.OnResultsReadyInterface {
        @Override
        public void onResults(ArrayList<String> results, Boolean partial) {
            if (results != null && results.size() > 0) {
                if (results.size() == 1) {
                    aMsg = new AMessage(results.get(0), pUserId, pUserName, pUserAvaR, true);
                    if (ASRManager.howToLine.equals("onStartingOfSpeech")){
                        aList.add(aMsg);
                    }else{
                        aList.set(aList.size() - 1, aMsg);
                    }
                    aAdapter.notifyDataSetChanged();             // refresh ListView when new messages coming
                    listView.setSelection(aList.size());   // go to the end of the ListView
                    Log.e(TAG, "line 269, onresults " + ASRManager.howToLine + " " + total6s);
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
        AlertDialog dialog = new AlertDialog.Builder(ChatListView.this)
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