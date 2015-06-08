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
	String path = null;

	@Override
	public void onCreate() {
		super.onCreate();
		// �̵���÷��̾ �ʱ�ȭ
		mMediaPlayer = new MediaPlayer();
		// �� ����� �Ϸ��ϸ� ���񽺸� �����Ų��
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
		// sdcard�� �ִ� test.mp3 �� ã�ư˻�
		path = intent.getStringExtra("path");
		
		/*
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			mMp3Path = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/tears.mp3";
			File mp3file = new File(mMp3Path);
			if (mp3file.exists()) {
				// mp3������ ������ ���������
				new Thread(mRun).start();
			}
		}
		*/
		if(path!= null){
			new Thread(mRun).start();
		}
		
		return START_STICKY;
	}

	Runnable mRun = new Runnable() {
		public void run() {
			try {
				// �̵���÷��̾� ���
				//mMediaPlayer.setDataSource(mMp3Path);
				mMediaPlayer.setDataSource(path);
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