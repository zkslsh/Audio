package com.example.audioplayer1;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
	MediaPlayer mMediaPlayer;
	String mMp3Path = "";
	String path = null;
	
	private final IBinder mBinder = new LocalBinder();
	private final Random mGenerator = new Random();
	

	public class LocalBinder extends Binder{
		MyService getService(){
			return MyService.this;
		}
	}


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
		// sdcard�� �ִ� tears.mp3 �� ã�ư˻�
		path = intent.getStringExtra("path");
		
		if(path!= null){
			try {
				mMediaPlayer.setDataSource(path);
				mMediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mMediaPlayer.start();
					
				}
			});

		}
		return START_STICKY;
	}

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
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	
	public int getRandomNumber(){
		return mGenerator.nextInt(100);
	}
	
	public void setMusicForward(){
		mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() + 2000);
	}
	
	public void setMusicRewind(){
		mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 2000);
	}
	
}