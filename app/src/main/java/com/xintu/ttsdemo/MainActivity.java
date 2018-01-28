package com.xintu.ttsdemo;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xintu.ttsdemo.biz.TTSBiz;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSpeak1, btnSpeak2, btnSpeak3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TTSBiz.getInstance().init(this);
        btnSpeak1 = (Button) findViewById(R.id.speak1);
        btnSpeak2 = (Button) findViewById(R.id.speak2);
        btnSpeak3 = (Button) findViewById(R.id.speak3);

        btnSpeak1.setOnClickListener(this);
        btnSpeak2.setOnClickListener(this);
        btnSpeak3.setOnClickListener(this);

    }

    public boolean isLoading = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.speak1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoading) {
                            return;
                        }
                        isLoading = true;
                        TTSBiz.getInstance().getFiles("/mnt/sdcard2/DCIM/LOP/back/");
                        isLoading = false;
                    }
                }).start();

//                TTSBiz.getInstance().setQueueMode(TextToSpeech.QUEUE_FLUSH);
//                TTSBiz.getInstance().speak1("123456789");
                break;
            case R.id.speak2:
                TTSBiz.getInstance().setQueueMode(TextToSpeech.QUEUE_ADD);
                TTSBiz.getInstance().speak1("987654321", TTSBiz.getInstance().utteranceId);
                break;
            case R.id.speak3:
                TTSBiz.getInstance().speak2("abcdefghijklmn", TTSBiz.getInstance().utteranceId);
                break;
        }
    }
}
