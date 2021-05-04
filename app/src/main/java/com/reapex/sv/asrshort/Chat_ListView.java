package com.reapex.sv.asrshort;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.asr.MLAsrListener;
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer;
import com.reapex.sv.BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.db.AMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat_ListView extends BaseActivity{
    static ASRListener     asrListener;        // = new ASRListener();
    static MLAsrRecognizer asrRecognizer;    // = MLAsrRecognizer.createAsrRecognizer();    //a 用户调用接口创建一个语音识别器。

    static String pUserId, pUserName;
    static int    pUserAvaR;

    List<AMessage>    aList; // = new ArrayList<AMessage>();
    ListView          aListView;
    MyListViewAdapter aAdapter;
    Intent            mIntent;

    boolean first = true;

    ImageView imageViewRecording;
    AnimationDrawable animationRecording;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat_list_view);               //a_activity_chat is listview

        pUserId   = getIntent().getStringExtra("from1");        ((TextView)findViewById(R.id.text_view_from_user_name)).setText(pUserName);
        pUserName = getIntent().getStringExtra("from2");
        pUserAvaR = getIntent().getIntExtra("from3", R.mipmap.bg_me_press); //default bg_me_press

        imageViewRecording  = findViewById(R.id.image_view_voice_recording_anim);
        aListView           = findViewById(R.id.list_view_message);

        aList    = new ArrayList<>(Arrays.asList(new AMessage("大家说，我听，您看。", "800", "你说", R.mipmap.default_user_avatar, false)));
        aAdapter = new MyListViewAdapter(Chat_ListView.this, aList);
        aListView.setAdapter(aAdapter);

        asrListener  = new ASRListener();
        asrRecognizer= MLAsrRecognizer.createAsrRecognizer(this);    //a 用户调用接口创建一个语音识别器。
        asrRecognizer.setAsrListener(asrListener);                        //b 绑定个listener
        mIntent = new Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH);
        mIntent.putExtra(MLAsrConstants.LANGUAGE, "zh-CN").putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX);
    }

    public void myClick(View view) {
        if (view.getId() == R.id.button_start) {
            asrRecognizer.startRecognizing(mIntent);

            animationRecording = (AnimationDrawable) imageViewRecording.getDrawable();
            animationRecording.start();
            ((TextView)findViewById(R.id.text_view_recording)).setText(getString(R.string.tv_click_to_stop));
            findViewById(R.id.button_stop).setVisibility(View.VISIBLE);
            findViewById(R.id.button_start).setVisibility(View.INVISIBLE);
        }else if (view.getId() == R.id.button_stop) {
            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            ((TextView)findViewById(R.id.text_view_recording)).setText(getString(R.string.asr_start_recording));
            findViewById(R.id.button_stop).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_start).setVisibility(View.VISIBLE);
        }
    }

    protected class ASRListener implements MLAsrListener {ArrayList<String> listResults = new ArrayList<>();
        // 从MLAsrRecognizer接收到持续语音识别的文本，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        // Bundle中携带了识别后的文本信息，文本信息以String类型保存在以MLAsrRecognizer.RESULTS_RECOGNIZING为key的value中。
        @Override
        public void onRecognizingResults(Bundle bundleResults) {
            if (bundleResults != null) {
                listResults.clear();
                listResults.add(bundleResults.getString(MLAsrRecognizer.RESULTS_RECOGNIZING));
                if (listResults != null && listResults.size() > 0) {
                    AMessage aMsg = new AMessage(listResults.get(0), pUserId, pUserName, pUserAvaR, true);
                    if (first) {
                        aList.add(aMsg);
                        first = false;
                    }else {
                        aList.set(aList.size() - 1, aMsg);
                    }
                    aAdapter.notifyDataSetChanged();           // refresh ListView when new messages coming
                    aListView.setSelection(aList.size());       // go to the end of the ListView
                }
                Log.e("Chat_ListView>>>>>>", "onRecognizingResults 53 " + bundleResults);    //Bundle[{results_recognizing=一颗}]
            }
        }

        @Override // 收尾。语音识别的文本数据，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        public void onResults(Bundle results) {


            first = true;
            if (asrRecognizer!=null) {
                asrRecognizer.destroy();
                asrRecognizer=null;
                asrRecognizer= MLAsrRecognizer.createAsrRecognizer(Chat_ListView.this);    //a 用户调用接口创建一个语音识别器。
                asrRecognizer.setAsrListener(asrListener);                        //b 绑定个listener
            }
            asrRecognizer.startRecognizing(mIntent);
            Log.e("Chat_ListView>>>3>>>", asrRecognizer.toString()+" listener " + asrListener.toString());

        }

        @Override //Log.e(">>>>>>", "onVoiceDataReceived-- data.length=" + length); Log.e(">>>>>>", "onVoiceDataReceived-- energy =" + energy);
        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {int length = data == null ? 0 : data.length; }
        @Override // If you don't add this, there will be no response after you cut the network
        public void onError(int error, String errorMessage) {Snackbar.make(imageViewRecording, getString(R.string.no_internet), Snackbar.LENGTH_SHORT);}

        @Override
        public void onState(int state, Bundle params) {
            if (state == MLAsrConstants.STATE_NO_SOUND_TIMES_EXCEED) {
                Log.e("Chat_ListView>>>>>>", "onState: 6s内没有检测到结果3:" + state + " total6s: ");
            }else if(state == MLAsrConstants.STATE_NO_SOUND) {
                Log.e("Chat_ListView>>>>>>", "onState: 3s内没有检测到没有说话2:" + state);
            }else if(state == MLAsrConstants.STATE_LISTENING) {
                Log.e("Chat_ListView>>>>>>", "onState: 录音机已经准备好1:" + state);
            }else{
                Log.e("Chat_ListView>>>>>>", "onState: 其他:" + state);
            }
        }

        @Override   //4
        public void onStartListening() {
            Log.e("Chat_ListView>>>>>>", "3 录音器开始接收声音。onStartListening--"+ "   #" );
        }

        @Override
        public void onStartingOfSpeech() {
            Log.e("Chat_ListView>>>>>>", "5 用户开始讲话，即语音识别器检测到用户开始讲话。 onStartingOfSpeech--");
        }
    }

    public void back(View view) {finish();}       // finish() will invoke onPause();

    @Override
    protected void onPause() {              // backend run, stop ASR
        super.onPause();
        stopASR("onPause");
    }

    void stopASR(String src){
//        if (oASRManager != null) {
//            oASRManager.destroy();
//            oASRManager = null;        }
        if (src.equals("onPause")) {
            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            findViewById(R.id.button_stop).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_start).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.text_view_recording)).setText(getString(R.string.asr_back));
        }else if (src.equals("button_stop")) {
            if (animationRecording != null && animationRecording.isRunning()) {animationRecording.stop();}
            findViewById(R.id.button_stop).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_start).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.text_view_recording)).setText(getString(R.string.asr_start_recording));
        }else if (src.equals("on6s")) {
            ((TextView)findViewById(R.id.text_view_recording)).setText(getString(R.string.asr_sleeping));
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
}