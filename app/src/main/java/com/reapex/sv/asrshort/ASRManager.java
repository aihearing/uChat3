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

    protected Context           mContext;
    protected CallBackInterface iCallBackInterface;  //2 接口变量


    public void destroy() {
        Log.d(TAG, "onDestroy 109");
    }

    public interface CallBackInterface {
        //1 定义接口和接口中的方法
        void onResults(ArrayList<String> results);
        void onFinish();
        void onError(int error);
    }

}
