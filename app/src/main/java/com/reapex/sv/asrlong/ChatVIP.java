package com.reapex.sv.asrlong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConstants;
import com.reapex.sv.R;

import java.util.ArrayList;

public class ChatVIP extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    // ASR
    RTTManager mRealTimeManager;
    private static final String LANGUAGE_ZH = MLSpeechRealTimeTranscriptionConstants.LAN_ZH_CN;
    private static final String LANGUAGE_EN = MLSpeechRealTimeTranscriptionConstants.LAN_EN_US;
    private static final String LANGUAGE_FR = MLSpeechRealTimeTranscriptionConstants.LAN_FR_FR;
    private String mLanguage = LANGUAGE_ZH;

    ArrayList<String> mArrayList;
    private volatile StringBuffer recognizerResult = new StringBuffer();

    //recyclerView
    MaterialButton mBtnStart, mBtnStop;
    AnimationDrawable mAnimationMic;
    TextView mTextViewBottom;
    ImageView mImageViewBottom;
    MyRecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat_vip);
        Log.e(TAG, "启动.");

        mRecyclerView = findViewById(R.id.recycler_view);
        mBtnStart     = findViewById(R.id.button_start);
        mBtnStop      = findViewById(R.id.button_stop);
        mImageViewBottom= findViewById(R.id.image_view_voice_recording_anim);
        mTextViewBottom = findViewById(R.id.text_view_recording);
        mTextViewBottom.setMovementMethod(new ScrollingMovementMethod());

        // 1. data to populate the RecyclerView with
        mArrayList = new ArrayList<>();
        mArrayList.add("Horse");

        // 2. set up the RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRecyclerViewAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
    }

    // click buttons
    public void back(View view) {finish();}
    public void myClick(View view) {
        if (view.getId() == R.id.button_start) {
            mBtnStop.setVisibility(View.VISIBLE);
            mBtnStart.setVisibility(View.INVISIBLE);

            mAnimationMic = (AnimationDrawable) mImageViewBottom.getDrawable();
            mAnimationMic.start();
            mTextViewBottom.setGravity(Gravity.CENTER);
            mTextViewBottom.setText(getString(R.string.tv_click_to_stop));

            if (mRealTimeManager == null) {
                Log.e(TAG, "1");
                setSpeechListener();
            } else if (!mRealTimeManager.ismIsListening()) {
                mRealTimeManager.destroy();
                setSpeechListener();
            }

        }else if (view.getId() == R.id.button_stop) {
            mBtnStop.setVisibility(View.INVISIBLE);
            mBtnStart.setVisibility(View.VISIBLE);

            if (mAnimationMic != null && mAnimationMic.isRunning()) {
                mAnimationMic.stop();}
            mTextViewBottom.setGravity(Gravity.CENTER);
            mTextViewBottom.setText(getString(R.string.asr_start_recording));

            if (mRealTimeManager != null) {
                if (recognizerResult.length() > 0) {
                    recognizerResult.delete(0, recognizerResult.length() - 1);
                }
                mTextViewBottom.setGravity(Gravity.CENTER);
                mTextViewBottom.setText(getString(R.string.destroied));

                MLSpeechRealTimeTranscription.getInstance().destroy();
                mRealTimeManager = null;
            }

        }
    }

    // asr
    private void setSpeechListener() {
        mRealTimeManager = new RTTManager(this, mLanguage, new RTTManager.OnResultsReady() {
            @Override
            public void onRecognizingResults(ArrayList<String> results, int status) {
                if (results != null && results.size() > 0) {
                    if (results.size() == 1) {
                        mTextViewBottom.setGravity(Gravity.BOTTOM);
                        mTextViewBottom.append(results.get(0));
                        if (status == RTTManager.RESULT_FINAL) {
                            mArrayList.add(results.get(0));
                            mTextViewBottom.setText("");
                        }
                        mAdapter.notifyDataSetChanged();                           // refresh ListView when new messages coming
                        mRecyclerView.smoothScrollToPosition(mArrayList.size());   // go to the end of the ListView
                    } else {
                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String result : results) {
                            sb.append(result).append("\n");
                        }
                        //resultTv.setText(sb.toString());
                        mArrayList.add(results.get(0));

                        if (status == RTTManager.RESULT_FINAL) {
                            if (!TextUtils.isEmpty(recognizerResult.toString())) {
                                //recognizerResult.append(",");
                            }
                            recognizerResult.append(sb.toString());
                            // errorTv.setText(recognizerResult.toString());
                        }
                    }
                }
            }

            @Override
            public void onError(int error) {
                String tip = getPrompt(error);
                if (!TextUtils.isEmpty(tip)) {
                    Log.d(TAG, "onError: " + error + " Update page: " + tip);
                    tip = "ERROR: " + tip;
                    //errorTv.setVisibility(View.VISIBLE);
                    //errorTv.setText(tip);
                    //去掉 you may speak now
                    //resultTv.setText("");
/*                    if (getString(R.string.you_may_speak).equals(resultTv.getText().toString())) {
                        resultTv.setText("");
                    }*/
                }
            }
        });
    }

    private String getPrompt(int errorCode) {
        Log.d(TAG, "Error Code： " + errorCode + " Other errors");
        String error_text;
        switch (errorCode) {
            case MLSpeechRealTimeTranscriptionConstants.ERR_NO_NETWORK:
                error_text = getString(R.string.no_intnet);
                break;
            case MLSpeechRealTimeTranscriptionConstants.ERR_SERVICE_UNAVAILABLE:
                error_text = getString(R.string.no_service);
                break;
            case MLSpeechRealTimeTranscriptionConstants.ERR_SERVICE_CREDIT:
                error_text = getString(R.string.no_balance);
                break;
            default:
                error_text = getString(R.string.errorcode) + errorCode + ",  " + getString(R.string.Others);
                break;
        }
        return error_text;
    }
}