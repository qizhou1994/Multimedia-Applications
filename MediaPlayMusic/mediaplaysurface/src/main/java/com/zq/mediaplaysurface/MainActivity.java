package com.zq.mediaplaysurface;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SurfaceView surfaceView;
    ImageButton play, pause, stop;
    MediaPlayer mPlayer;
    // 记录当前视频的播放位置
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取界面中的三个按钮
        play = (ImageButton) findViewById(R.id.play);
        pause = (ImageButton) findViewById(R.id.pause);
        stop = (ImageButton) findViewById(R.id.stop);
        // 为三个按钮的单击事件绑定事件监听器
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        // 创建MediaPlayer
        mPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        // 设置播放时打开屏幕
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(new SurfaceListener());
        getPression();
    }

    /**
     * 申请获得权限
     */
    private void getPression() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//检查是否有了权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                //没有权限即动态申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
            }
        }
    }

    @Override
    public void onClick(View source) {
        try {
            switch (source.getId()) {
                // 播放按钮被单击
                case R.id.play:
                    play();
                    break;
                // 暂停按钮被单击
                case R.id.pause:
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    } else {
                        mPlayer.start();
                    }
                    break;
                // 停止按钮被单击
                case R.id.stop:
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                    }
                    break;
                default:
                    ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() throws IOException {
        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置需要播放的视频
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;//视频
        //视频的位置
        mPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VID_20180503_115636.mp4");
        // 把视频画面输出到SurfaceView
        mPlayer.setDisplay(surfaceView.getHolder());  // ①
        mPlayer.prepare();
        // 获取窗口管理器
        WindowManager wManager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕大小
        wManager.getDefaultDisplay().getMetrics(metrics);
        // 设置视频保持纵横比缩放到占满整个屏幕
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels
                , mPlayer.getVideoHeight() * metrics.widthPixels
                / mPlayer.getVideoWidth()));
        mPlayer.start();
    }

    private class SurfaceListener implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (position > 0) {
                try {
                    // 开始播放
                    play();
                    // 并直接从指定位置开始播放
                    mPlayer.seekTo(position);
                    position = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    // 当其他Activity被打开时，暂停播放
    @Override
    protected void onPause() {
        if (mPlayer.isPlaying()) {
            // 保存当前的播放位置
            position = mPlayer.getCurrentPosition();
            mPlayer.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 停止播放
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        // 释放资源
        mPlayer.release();
        super.onDestroy();
    }
}
