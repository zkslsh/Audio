package com.example.audioplayer1;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	MediaPlayer mMediaPlayer;
	String mMp3Path = "";

	@Override
	public void onCreate() {
		super.onCreate();
		// 미디어플레이어를 초기화
		mMediaPlayer = new MediaPlayer();
		// 곡 재생이 완료하면 서비스를 종료시킨다
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "service start..", Toast.LENGTH_SHORT).show();
		// sdcard에 있는 test.mp3 을 찾아검사
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			mMp3Path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/tears.mp3";
			File mp3file = new File(mMp3Path);
			if (mp3file.exists()) {
				// mp3파일이 있으면 쓰레드실행
				new Thread(mRun).start();
			}
		}
		return START_STICKY;
	}

	Runnable mRun = new Runnable() {
		public void run() {
			try {
				// 미디어플레이어 재생
				mMediaPlayer.setDataSource(mMp3Path);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onDestroy() {
		Toast.makeText(this, "service destroy..", Toast.LENGTH_SHORT).show();
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}