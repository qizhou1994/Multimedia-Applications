package com.zq.mediaplay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.mediaplay.base.BaseActivity;
import com.zq.mediaplay.camera.CameraPictureActivity;
import com.zq.mediaplay.mediarecorder.AudioRecordActivity;
import com.zq.mediaplay.mediarecorder.MediaRecorderActivity;
import com.zq.mediaplay.soundpool.SoundPoolActivity;
import com.zq.mediaplay.surfacemediaplayer.SurfaceMediaPlayerActivity;
import com.zq.mediaplay.videoviewtest.VideoViewActivity;


public class MainActivity extends BaseActivity {

    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = new LinearLayout(this);//一个线性布局
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
        initAddView("音乐",MediaMusicActivity.class);
        initAddView("MediaRecord录音",MediaRecorderActivity.class);
        initAddView("音效",SoundPoolActivity.class);
        initAddView("videoview视频",VideoViewActivity.class);
        initAddView("surface视频",SurfaceMediaPlayerActivity.class);
        initAddView("camera拍照",CameraPictureActivity.class);
        initAddView("AudioRecord录音 ",AudioRecordActivity.class);

    }

    /**
     * 添加按钮
     */
    private void initAddView(final String title, final Class clas) {
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGoThen(clas);
            }
        });
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setGravity(Gravity.CENTER);


        textView.setHeight(100);

        linearLayout.addView(textView);
        //添加分割线
        TextView textView1 = new TextView(this);

        textView1.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1));
        textView1.setBackgroundColor(Color.BLACK);
        linearLayout.addView(textView1);
    }

}
