package com.zq.mediaplay.videoviewtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;


import com.zq.mediaplay.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ViewViewActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mController;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_videoview);
        // 获取界面上VideoView组件
        videoView = (VideoView) findViewById(R.id.video);
        // 创建MediaController对象
        mController = new MediaController(this);
        //获取列表
        listView = (ListView) findViewById(R.id.list);
        //获取手机中的视频 并初始化listview
        getVideoInfoList();
        Log.e("Video ", "Video = " + MediaStore.Video.Media.EXTERNAL_CONTENT_URI);


        getPression();
       /* File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/VID_20180503_115636.mp4");
        Uri uri = Uri.fromFile(file);*/
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(ViewViewActivity.this, "com.zq.mediaplaymusic.videoviewtest",
                    file);    //android 7.0添加fileProvider才可获取到正确的uri
   /*     }*//*
            videoView.setVideoURI(uri);  // ①
            // 设置videoView与mController建立关联
            videoView.setMediaController(mController);  // ②
            // 设置mController与videoView建立关联
            mController.setMediaPlayer(videoView);  // ③
            // 让VideoView获取焦点
            videoView.requestFocus();

            videoView.start();*/


//        getVideoInfoList();
    }

    /**
     * 初始化listview
     *
     * @param videoInfos
     */
    private void initList(final List<VideoInfo> videoInfos) {
        listView.setAdapter(new ArrayAdapter<VideoInfo>(this, R.layout.item, videoInfos));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoView.setVideoPath(videoInfos.get(position).getPath());  // ①
                // 设置videoView与mController建立关联
                videoView.setMediaController(mController);  // ②
                // 设置mController与videoView建立关联
                mController.setMediaPlayer(videoView);  // ③
                // 让VideoView获取焦点
                videoView.requestFocus();
                videoView.start();
            }
        });
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

    private List<VideoInfo> mVideoInfoList = new ArrayList<>();

    /**
     * 获取本地视频资源是耗时操作！！！！可以不使用rxjava 线程中操作也可以
     * 获取手机中的视频 并初始化listview
     */
    private void getVideoInfoList() {
        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);

        if (cursor == null) {
            return;
        }

        Observable.just(cursor)
                .map(new Func1<Cursor, List<VideoInfo>>() {
                    @Override
                    public List<VideoInfo> call(Cursor cursor) {
                        return cursorToList(cursor);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<VideoInfo>>() {
                    @Override
                    public void call(List<VideoInfo> videoInfos) {
                        Log.e("Video ", "Video videoInfos = " + videoInfos.get(1).getPath());
                 /*       File file = new File(videoInfos.get(1).getPath());
                        Uri uri = Uri.fromFile(file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uri = FileProvider.getUriForFile(ViewViewActivity.this, "com.zq.mediaplaymusic.videoviewtest",
                                    file);    //android 7.0添加fileProvider才可获取到正确的uri
                        }
                        Log.e("Video ", "Video videoInfos = " + videoInfos.get(1).getPath());
                        videoView.setVideoPath(uri.getPath());  // ①
                        // 设置videoView与mController建立关联
                        videoView.setMediaController(mController);  // ②
                        // 设置mController与videoView建立关联
                        mController.setMediaPlayer(videoView);  // ③
                        // 让VideoView获取焦点
                        videoView.requestFocus();*/
                        initList(videoInfos);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.e("error", "错误信息 失败请求");
                    }
                });
    }

    /**
     * 将扫描的视频添加到集合中
     *
     * @param cursor
     * @return
     */
    private List<VideoInfo> cursorToList(Cursor cursor) {

        mVideoInfoList.clear();
        VideoInfo videoInfo;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String album = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));

            String artist = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
            String displayName = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String mimeType = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            String path = cursor
                    .getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor
                    .getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long size = cursor
                    .getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

            videoInfo = new VideoInfo(id, title, album, artist, displayName,
                    mimeType, path, size, duration);
            mVideoInfoList.add(videoInfo);
        }
        cursor.close();

        return mVideoInfoList;
    }

}
