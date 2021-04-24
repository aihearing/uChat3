package com.reapex.sv.asrlong;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscription;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConfig;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionConstants;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionListener;
import com.huawei.hms.mlsdk.speechrtt.MLSpeechRealTimeTranscriptionResult;

import java.util.ArrayList;
//RealTimeTranscriptionManager
public class RTTManager {

    private final static String TAG = "RealTimeTranscription";
    public static String howToLine;

    public static final int RESULT_FINAL = 2;
    public static final int RESULT_RECEVING = 3;

    private AudioManager mAudioManager;

    private boolean mIsListening;
    private OnResultsReady mListener;
    private ArrayList<String> mResultsList = new ArrayList<>();

    private MLSpeechRealTimeTranscription mlAsrLongRecognizer;

    private String language;

    public RTTManager(Context context, String mLanguage, OnResultsReady listener) {
        language = mLanguage;
        Log.e(TAG, "created.");
        try {
            mListener = listener;
        } catch (ClassCastException e) {
            Log.e(TAG, e.toString());
        }

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLongRecognizer();
            }
        }).start();
    }

    private void startLongRecognizer() {
        Log.e(TAG, "1");
        MLSpeechRealTimeTranscriptionConfig config = new MLSpeechRealTimeTranscriptionConfig.Factory()
                .setLanguage(language)
                .enablePunctuation(true)
                .enableSentenceTimeOffset(true)
                .enableWordTimeOffset(true)
                // Set the usage scenario to shopping,Currently, only Chinese scenarios are supported.
                // .setScenes(MLSpeechRealTimeTranscriptionConstants.SCENES_SHOPPING)
                .create();
        MLSpeechRealTimeTranscription.getInstance().setRealTimeTranscriptionListener(new SpeechRecognitionListener());
        MLSpeechRealTimeTranscription.getInstance().startRecognizing(config);
    }

    public void destroy() {
        Log.e(TAG, "2");
        if (mlAsrLongRecognizer != null) {
            mlAsrLongRecognizer.destroy();
            mlAsrLongRecognizer = null;
        }
    }

    protected class SpeechRecognitionListener implements MLSpeechRealTimeTranscriptionListener {
        @Override
        public void onStartListening() {
            Log.d(TAG, "onStartListening");
            Log.e(TAG, "3");

        }

        @Override
        public void onStartingOfSpeech() {
            howToLine = "onStartingOfSpeech";
            Log.e(TAG, "4 onStartingOfSpeech");
        }

        @Override
        public void onVoiceDataReceived(byte[] data, float energy, Bundle bundle) {
            int length = data == null ? 0 : data.length;
            Log.d(TAG, "5 onVoiceDataReceived data.length=" + length);
        }

        @Override
        public void onRecognizingResults(Bundle partialResults) {
            Log.e(TAG, "6");

            if (partialResults != null && mListener != null) {
                Log.e(TAG, "7");

                mResultsList.clear();
                mResultsList.add(partialResults.getString(MLSpeechRealTimeTranscriptionConstants.RESULTS_RECOGNIZING));
                boolean isFinal = partialResults.getBoolean(MLSpeechRealTimeTranscriptionConstants.RESULTS_PARTIALFINAL);
                if (isFinal) {
                    Log.e(TAG, "8");

                    String result = partialResults.getString(MLSpeechRealTimeTranscriptionConstants.RESULTS_RECOGNIZING);
                    Log.d(TAG, "onRecognizingResults is " + result);
                    mListener.onRecognizingResults(mResultsList, RESULT_FINAL);
                    howToLine = "Partial";

                    ArrayList<MLSpeechRealTimeTranscriptionResult> wordOffset = partialResults.getParcelableArrayList(MLSpeechRealTimeTranscriptionConstants.RESULTS_WORD_OFFSET);
                    ArrayList<MLSpeechRealTimeTranscriptionResult> sentenceOffset = partialResults.getParcelableArrayList(MLSpeechRealTimeTranscriptionConstants.RESULTS_SENTENCE_OFFSET);

                    if (wordOffset != null) {
                        for (int i = 0; i < wordOffset.size(); i++) {
                            MLSpeechRealTimeTranscriptionResult remoteResult = wordOffset.get(i);
                            Log.d(TAG, "onRecognizingResults word offset is " + i + " ---> " + remoteResult.toString());
                        }
                    }

                    if (sentenceOffset != null) {
                        for (int i = 0; i < sentenceOffset.size(); i++) {
                            MLSpeechRealTimeTranscriptionResult remoteResult = sentenceOffset.get(i);
                            Log.d(TAG, "onRecognizingResults sentence offset is " + i + " ---> " + remoteResult.toString());
                        }
                    }
                } else {
                    mListener.onRecognizingResults(mResultsList, RESULT_RECEVING);
                    Log.e(TAG, "9");

                }
            }
        }

        @Override
        public void onError(int error, String errorMessage) {
            // If this parameter is not added,
            // the system does not respond after the network is disconnected and the recording is performed again.
            Log.e(TAG, "10");

            mIsListening = false;
            if (mListener != null) {
                mListener.onError(error);
            }
        }

        @Override
        public void onState(int state, Bundle params) {
            Log.e(TAG, "---------> onState is " + state);
            if (state == MLSpeechRealTimeTranscriptionConstants.STATE_SERVICE_RECONNECTING) { // webSocket Reconnecting
                Log.e(TAG, "onState webSocket reconnect ");
            } else if (state == MLSpeechRealTimeTranscriptionConstants.STATE_LISTENING) { // 录音机已经准备好。
                Log.e(TAG, "STATE_LISTENING1");
            } else if (state == MLSpeechRealTimeTranscriptionConstants.STATE_SERVICE_RECONNECTED) { // webSocket Reconnection succeeded.
                Log.e(TAG, "onState webSocket reconnect success ");
            }
        }
    }

    public boolean ismIsListening() {
        return mIsListening;
    }

    public interface OnResultsReady {
        void onRecognizingResults(ArrayList<String> results, int status);
        void onError(int error);
    }
}
