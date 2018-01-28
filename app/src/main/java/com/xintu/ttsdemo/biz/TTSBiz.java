package com.xintu.ttsdemo.biz;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by xintu on 2018/1/24.
 */

public class TTSBiz implements TextToSpeech.OnInitListener {
    private static final String TAG = "TTSBiz";
    private TextToSpeech tts;
    private HashMap ttsListener;
    private int queueMode = TextToSpeech.QUEUE_ADD;
    public String utteranceId = "com.xintu.ttsdemo";

    private static TTSBiz ttsBiz = null;


    public static TTSBiz getInstance() {
        if (ttsBiz == null) {
            ttsBiz = new TTSBiz();
        }
        return ttsBiz;
    }

    public void init(Context mContext) {
        tts = new TextToSpeech(mContext, this);
        ttsListener = new HashMap<String, String>();
        ttsListener.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                utteranceId);
    }


    public void speak1(String strText) {
        if (tts != null) {
            tts.speak(strText, queueMode, ttsListener);
        }
    }

    public void speak1(String strText, String autteranceId) {
        if (tts != null) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, autteranceId);
            param.put(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC + "");
//            param.put(TextToSpeech.Engine.KEY_PARAM_VOLUME,1+"");
//            param.put(TextToSpeech.Engine.KEY_PARAM_PAN,0+"");
            tts.speak(strText, queueMode, param);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void speak2(String strText, String autteranceId) {
        if (tts != null) {
            Bundle params = new Bundle();
            params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
//            params.putInt(TextToSpeech.Engine.KEY_PARAM_VOLUME,1);
//            params.putInt(TextToSpeech.Engine.KEY_PARAM_PAN,0);
            tts.speak(strText, queueMode, params, autteranceId);
        }
    }

    public void setSpeechRate(float rate) {
        if (rate > 1 || rate < 0) {
            return;
        }
        tts.setSpeechRate(rate);
    }

    /**
     * 设置播放模式
     *
     * @param mode
     * @value TextToSpeech.QUEUE_ADD, TextToSpeech.QUEUE_FLUSH
     */
    public void setQueueMode(int mode) {
        queueMode = mode;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "chinese not available");
            }
            setSpeechRate(1.0f);
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.d(TAG, "onStart=" + utteranceId);
                }

                @Override
                public void onDone(String utteranceId) {
                    Log.d(TAG, "onDone=" + utteranceId);
                }

                @Override
                public void onError(String utteranceId) {
                    Log.d(TAG, "onError=" + utteranceId);
                }
            });
        }
    }

    /**
     * 获取某一路径向所有的文件的大小，与demo无关
     *
     * @param dirpath
     */

    public void getFiles(String dirpath) {
        File filedir = new File(dirpath);
        File[] files = filedir.listFiles();
        Log.d(TAG, "path=" + dirpath + "files.size=" + files.length);

        for (File file : files) {
            if (file.isDirectory()) {
                getFiles(file.getAbsolutePath());
            } else {
                String url = file.getAbsolutePath();
                Log.d(TAG, "url: " + url + ",size=" + getDuration(url));
            }

        }
    }

    /**
     * 获取某一文件的大小
     *
     * @param url
     * @return
     */
    public long getDuration(String url) {
        String duration = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {

            retriever.setDataSource(url);

            duration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            Log.e("hello", ex.toString());
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                Log.e("hello", ex.toString());
            }
        }
        if (duration != null) {
            return Long.parseLong(duration);
        } else {
            return 0;
        }
    }
}
