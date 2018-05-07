# Multimedia-Applications
多媒体应用实践。视频、音频等



# 强调
	大多数时候，只是单纯的使用一个组件或实现某种简单功能的时候，是比较无脑的，直接调用api即可。复杂的地方一般在于优化或者是复杂的功能。这里只是简单的实现展示。

## 第一章节为音频实践：
	使用`MediaPlayer`播放音频。
	主要使用
	start():播放
	stop():停止播放
	pause():暂定播放
	如图
	![示例](http://github.com/qizhou1994/Multimedia-Applications/img/mediaplayer_.png "图片")
	播放使用 到的主要是MediaPlayer,其他的则为参数用于调试与展示视图。	

## 第二节为播放音效：
	使用`SoundPool`播放出来。  
	`MediaPlayer`与其对比：  
	资源占用过高，延迟时间较长。
	不支持多个音频同时播放。

## 第三节为视频播放。
	使用VideoView播放视频，还提供控制界面播放视频。
	使用`MediaPlayer`与SurfaceView一起播放视频。
	MediaPlayer用于播放视频的音频，而SurfaceView主要用于视频的播放。单独使用一个便只有一个功能有效。

## 第四节为录音。
	使用MediaRecorder录音。
	
 
	
	

 
 