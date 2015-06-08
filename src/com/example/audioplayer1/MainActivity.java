package com.example.audioplayer1;
/*
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.wid
*/

import android.media.MediaPlayer.OnCompletionListener;

import android.media.MediaPlayer.OnPreparedListener;

import android.media.MediaPlayer.OnSeekCompleteListener;

import android.app.Activity;

import android.media.MediaPlayer;

import android.os.Bundle;

import android.os.IBinder;

import android.os.RemoteException;

import android.util.Log;

import android.view.View;

import android.view.View.OnClickListener;

import android.os.Environment;

import android.widget.FrameLayout;

import android.widget.LinearLayout;

import android.widget.VideoView;

import android.widget.MediaController;

import android.widget.Button;

import android.content.ComponentName;

import android.content.ContentResolver;

import android.content.Context;

import android.content.Intent;

import android.content.ServiceConnection;

import android.content.SharedPreferences;

import android.content.SharedPreferences.Editor;

import android.database.Cursor;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;

import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;

import android.widget.BaseAdapter;

import android.widget.ImageView;

import android.widget.ListView;

import android.widget.SeekBar;

import android.widget.TextView;

import android.widget.Toast;



public class MainActivity extends Activity implements OnClickListener {

	private Button back_Button; // BACK ��ư
	private Button home_Button; // HOME ��ư
	private Button menu_Button; // MENU ��ư
	private Button function_Button; // FUNCTION ��ư
	private Button setting_Button; // SETTING ��ư
	private Button play_Button; // PLAY ��ư
	private Button rw_Button; // RW ��ư ( �����ΰ��� )
	private Button ff_Button; // FF ��ư ( �ڷΰ��� )
	private Button list_Button; // LIST ��ư
	private LinearLayout List;
	private LinearLayout LinearLayout;
	private Cursor videocursor;
	private int count;
	private ListView videolist;
	private int video_column_index;
	private String filename = null;
	private String timeText1 = null;
	private String timeText2 = null;
	private Intent intent;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button service_start = (Button) findViewById(R.id.play);
		Button service_end = (Button) findViewById(R.id.rw);
		List = (LinearLayout)findViewById(R.id.List);
		LinearLayout = (LinearLayout)findViewById(R.id.Right_Container);
		list_Button = (Button)findViewById(R.id.list);
		

		service_start.setOnClickListener(this);
		service_end.setOnClickListener(this);
		
	    list_Button.setOnClickListener(new OnClickListener(){

            public void onClick(View v){

                //list_Button.setSelected(true);

                //list_Button.setPressed(true);


                init_phone_video_grid();

            }
        });		
	    

	    LinearLayout.setVisibility(View.VISIBLE);
	    List.setVisibility(View.INVISIBLE);
	}
	




	public void onClick(View v) {
		if (v.getId() == R.id.play) {
			intent = new Intent(this, MyService.class);
			intent.putExtra("path", filename);
			startService(intent);
			

		} else if (v.getId() == R.id.rw) {
			stopService(new Intent(this, MyService.class));
		} 
		
	}
	

	static class ViewHolder{
		TextView txtTitle;
		TextView txtSize;
		ImageView thumbImage;
		TextView runningTime;
	}

	
	@SuppressWarnings("deprecation")
	private void init_phone_video_grid() {
		LinearLayout.setVisibility(View.INVISIBLE);
		List.setVisibility(View.VISIBLE); 
        
		System.gc();
		String[] proj = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.SIZE,
				MediaStore.Audio.Media.DURATION};
		videocursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
		count = videocursor.getCount();
		videolist = (ListView) findViewById(R.id.PhoneVideoList);
		videolist.setAdapter(new VideoAdapter(getApplicationContext()));
		videolist.setOnItemClickListener(videogridlistener);
	}
	
	private OnItemClickListener videogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			
			System.gc();
			video_column_index = videocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			videocursor.moveToPosition(position);
			
			filename  = videocursor.getString(video_column_index);
			//videoview.setVideoPath(filename);
			videocursor.moveToPosition(position);
			
			video_column_index = videocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
			//movie_Name.setText(videocursor.getString(video_column_index));
			videocursor.moveToPosition(position);
			
			video_column_index = videocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
			String iduration = videocursor.getString(video_column_index);
			
			long timeInmillisec = Long.parseLong(iduration);
			long duration = timeInmillisec / 1000;
			int hours = (int)(duration / 3600);
			int minutes = (int) ((duration - hours * 3600)/ 60);
			int seconds = (int) (duration - (hours * 3600 + minutes * 60));
	
			
			String time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
			//timeText2.setText(time);

			//videoview.requestFocus();
		
	
			
			List.setVisibility(View.INVISIBLE);
			LinearLayout.setVisibility(View.VISIBLE);
		}
	};

	public class VideoAdapter extends BaseAdapter {
		private Context vContext;

		public VideoAdapter(Context c) {
			vContext = c;
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			System.gc();
			ViewHolder holder;
			String id = null;

			
			convertView = null;
			if (convertView == null) {
				//
				convertView = LayoutInflater.from(vContext).inflate(
						R.layout.listitem, parent, false);
				holder = new ViewHolder();
				holder.txtTitle = (TextView) convertView
						.findViewById(R.id.txtTitle);
				holder.txtSize = (TextView) convertView
						.findViewById(R.id.txtSize);
				holder.runningTime = (TextView) convertView
						.findViewById(R.id.runningTime);				
				holder.thumbImage = (ImageView) convertView
						.findViewById(R.id.imgIcon);
				
				// ListView �� Display_name ǥ��
				video_column_index = videocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
				videocursor.moveToPosition(position);
				id = videocursor.getString(video_column_index);
				holder.txtTitle.setText(id);
				
				
				// ListView �� File Size ǥ��
				video_column_index = videocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
				videocursor.moveToPosition(position);
				String size = videocursor.getString(video_column_index);
				
				long lsize = Long.parseLong(size);
				double dsize = lsize/1000000;
				String ssize = String.format("%4.1f", dsize);
				holder.txtSize.setText(ssize + "Mb" +  " | ");
				
				// ListView �� ����ð� ǥ��
				video_column_index = videocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
				videocursor.moveToPosition(position);
				String iduration = videocursor.getString(video_column_index);

				String time;
				
				long timeInmillisec = Long.parseLong(iduration);
				long duration = timeInmillisec / 1000;
				int hours = (int)(duration / 3600);
				int minutes = (int) ((duration - hours * 3600)/ 60);
				int seconds = (int) (duration - (hours * 3600 + minutes * 60));
				
				time = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
				
				holder.runningTime.setText(time);
				videocursor.moveToPosition(position);
				
				String[] proj = { MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.DURATION};
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
						MediaStore.Audio.Media.DISPLAY_NAME + "=?",
						new String[] { id }, null);
				cursor.moveToFirst();
				long ids = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID));

				ContentResolver crThumb = getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				//MediaStore.Audio.
				//Bitmap curThumb = MediaStore.Images.Thumbnails(
						//crThumb, ids,Images.Thumbnails.MICRO_KIND.null);
				//Bitmap sizingBmp = Bitmap.createScaledBitmap(curThumb, 140, 90, true);
				//holder.thumbImage.setImageBitmap(curThumb);
				holder.thumbImage.setImageBitmap(null);
				//curThumb = null;
			}
			return convertView;
		}
	}

}

