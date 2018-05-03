package com.zq.mediaplay.mediarecorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zq.mediaplay.base.BaseActivity;
import com.zq.mediaplay.R;

import java.io.File;

public class RediaRecorderActivity extends BaseActivity
        implements View.OnClickListener {
    // 定义界面上的两个按钮
    ImageButton record, stop;
    // 系统的音频文件
    File soundFile;
    MediaRecorder mRecorder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redia_recorder);
        // 获取程序界面中的两个按钮
        record = (ImageButton) findViewById(R.id.record);
        stop = (ImageButton) findViewById(R.id.stop);
        // 为两个按钮的单击事件绑定监听器
        record.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (soundFile != null && soundFile.exists()) {
            // 停止录音
            mRecorder.stop();
            // 释放资源
            mRecorder.release();
            mRecorder = null;
        }
    }

    @Override
    public void onClick(View source) {
        switch (source.getId()) {
            // 单击录音按钮
            case R.id.record:
                if (!Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(this, "SD卡不存在，请插入SD卡！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Log.e("开始录音","开始录音");
                    // 创建保存录音的音频文件
                    soundFile = new File(Environment
                            .getExternalStorageDirectory().getCanonicalFile()
                            + "/sound.amr");
                    Log.e("开始录音","开始录音 = " + soundFile.getAbsolutePath());
                    mRecorder = new MediaRecorder();
                    // 设置录音的声音来源
                    mRecorder.setAudioSource(MediaRecorder
                            .AudioSource.MIC);
                    // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
                    mRecorder.setOutputFormat(MediaRecorder
                            .OutputFormat.THREE_GPP);
                    // 设置声音编码的格式
                    mRecorder.setAudioEncoder(MediaRecorder
                            .AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(soundFile.getAbsolutePath());
                    mRecorder.prepare();
                    // 开始录音
                    mRecorder.start();  // ①
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            // 单击停止按钮
            case R.id.stop:
                if (soundFile != null && soundFile.exists()) {
                    // 停止录音
                    mRecorder.stop();  // ②
                    // 释放资源
                    mRecorder.release();  // ③
                    mRecorder = null;
                }
                break;
            default:
                ;
        }
    }
}

