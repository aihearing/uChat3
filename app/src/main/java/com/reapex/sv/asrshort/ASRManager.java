package com.reapex.sv.asrshort;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.mlsdk.asr.MLAsrConstants;
import com.huawei.hms.mlsdk.asr.MLAsrListener;
import com.huawei.hms.mlsdk.asr.MLAsrRecognizer;

import java.util.ArrayList;

//SpeechRecognizerManager
public class ASRManager {
    final String TAG = this.getClass().getSimpleName();

    public static String howToLine;
    ASRListener mListenerSV;

    protected Context         mContext;
    protected MLAsrRecognizer oMLAsrRecognizer;
    private CallBackInterface iCallBackInterface;  //2 接口变量

    public ASRManager(Context pContext, CallBackInterface pInterface) {
        try {
            iCallBackInterface = pInterface;        //3 提供注册接口的方法 暴露接口给调用者；
        } catch (ClassCastException e) {            Log.e(TAG, e.toString());        }
        mContext = pContext;            //chat.class
        mListenerSV = new ASRListener();
        initReco();
    }

    protected void initReco(){
        oMLAsrRecognizer = MLAsrRecognizer.createAsrRecognizer(mContext);    //a 用户调用接口创建一个语音识别器。
        oMLAsrRecognizer.setAsrListener(mListenerSV);                        //b 绑定个listener
        Intent intent = new Intent(MLAsrConstants.ACTION_HMS_ASR_SPEECH);
        intent.putExtra(MLAsrConstants.LANGUAGE, "zh-CN")
              .putExtra(MLAsrConstants.FEATURE, MLAsrConstants.FEATURE_WORDFLUX);
        oMLAsrRecognizer.startRecognizing(intent);
    }

    protected class ASRListener implements MLAsrListener {ArrayList<String> pResultsList = new ArrayList<>();
        // 从MLAsrRecognizer接收到持续语音识别的文本，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        //Bundle中携带了识别后的文本信息，文本信息以String类型保存在以MLAsrRecognizer.RESULTS_RECOGNIZING为key的value中。
        @Override
        public void onRecognizingResults(Bundle partialResults) {
            if (partialResults != null) {
                pResultsList.clear();
                pResultsList.add(partialResults.getString(MLAsrRecognizer.RESULTS_RECOGNIZING));
                iCallBackInterface.onResults(pResultsList);      //4 接口变量调用被实现的接口方法；
                howToLine="Partial";
                Log.e(TAG, "onRecognizingResults 53 " + partialResults);    //Bundle[{results_recognizing=一颗}]
            }
        }

        // 收尾。语音识别的文本数据，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        // long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        @Override
        public void onResults(Bundle results) {
            if (results != null) {
                pResultsList.clear();
                pResultsList.add(results.getString(MLAsrRecognizer.RESULTS_RECOGNIZED));
                Log.e(TAG, "initReco " + "total6s: ");
            }
        }

        @Override
        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {
            int length = data == null ? 0 : data.length;
//            Log.e(TAG, "onVoiceDataReceived-- data.length=" + length);
  //          Log.e(TAG, "onVoiceDataReceived-- energy =" + energy);
        }

        @Override
        public void onError(int error, String errorMessage) {
            // If you don't add this, there will be no response after you cut the network
            if (iCallBackInterface != null) {
                iCallBackInterface.onError(error);
            }
        }

        @Override
        //通知应用状态发生改变，该接口并非运行在主线程中，返回结果需要在子线程中处理。
        //STATE_LISTENING        //onState回调中的状态码，表示录音机已经准备好。      //Constant Value：1
        //STATE_NO_NETWORK        //onState回调中的状态码，表示当前环境没有网络。      //Constant Value：7
        //STATE_NO_SOUND        //onState回调中的状态码，表示3s内没有检测到没有说话。  //Constant Value：2
        //STATE_NO_SOUND_TIMES_EXCEED //onState回调中的状态码，表示6s内没有检测到结果。//Constant Value：3
        //STATE_NO_UNDERSTAND        //onState回调中的状态码，表示当前帧云端检测失败。 //Constant Value：6
        //STATE_WAITING //onState回调中的状态码，表示第一次发送数据时在弱网情况下。     //Constant Value：9
        public void onState(int state, Bundle params) {
            if (state == MLAsrConstants.STATE_NO_SOUND_TIMES_EXCEED) {
                if (iCallBackInterface != null) {
                    iCallBackInterface.onFinish(); }
                Log.e(TAG, "onState: 6s内没有检测到结果3:" + state + " total6s: ");
            }else if(state == MLAsrConstants.STATE_NO_SOUND) {
                Log.e(TAG, "onState: 3s内没有检测到没有说话2:" + state);
            }else if(state == MLAsrConstants.STATE_LISTENING) {
                Log.e(TAG, "onState: 录音机已经准备好1:" + state);
            }else{
                Log.e(TAG, "onState: 其他:" + state);
            }
        }

        @Override   //4
        public void onStartListening() {
            Log.e(TAG, "3 录音器开始接收声音。onStartListening--"+ "   #" );
        }

        @Override
        public void onStartingOfSpeech() {
            howToLine = "onStartingOfSpeech";
            Log.e(TAG, "5 用户开始讲话，即语音识别器检测到用户开始讲话。 onStartingOfSpeech--"+ howToLine);
        }
    }

    public void destroy() {
        Log.d(TAG, "onDestroy 109");
        if (oMLAsrRecognizer != null) {
            oMLAsrRecognizer.destroy();
            oMLAsrRecognizer = null;
        }
    }

    public interface CallBackInterface {
        //1 定义接口和接口中的方法
        void onResults(ArrayList<String> results);
        void onFinish();
        void onError(int error);
    }
}
