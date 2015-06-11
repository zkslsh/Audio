package com.example.audioplayer1;

import java.lang.reflect.GenericSignatureFormatError;

import com.example.audioplayer1.MyService.LocalBinder;

import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Environment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Button;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
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

	private Button back_Button; // BACK 버튼
	private Button home_Button; // HOME 버튼
	private Button menu_Button; // MENU 버튼
	private Button function_Button; // FUNCTION 버튼
	private Button setting_Button; // SETTING 버튼
	private Button play_Button; // PLAY 버튼
	private Button rw_Button; // RW 버튼
	private Button ff_Button; // FF 버튼
	private Button list_Button; // LIST 버튼
	private LinearLayout List;
	private LinearLayout LinearLayout;
	private Cursor audiocursor;
	private int count;
	private ListView PhoneAideoList;
	private int audio_column_index;
	private String filename = null;
	private String timeText1 = null;
	private String timeText2 = null;
	private Intent intent;
    private MyService mService;
    boolean mBound = false;
    private ServiceConnection mConnection; 
    private TextView artist_View;
    private TextView album_View;
    private TextView year_View;
    private TextView filesize_View;
    private TextView position_View;
    private ImageView image_View;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	LocalBinder binder = (LocalBinder) service;
	            mService = binder.getService();
	            mBound = true;
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	            mBound = false;
	        }
	    };		

		Button service_start = (Button) findViewById(R.id.play);
		Button service_end = (Button) findViewById(R.id.rw);
		Button service_ff = (Button) findViewById(R.id.ff);
		List = (LinearLayout) findViewById(R.id.List);
		LinearLayout = (LinearLayout) findViewById(R.id.Right_Container);
		list_Button = (Button) findViewById(R.id.list);
		
		artist_View = (TextView)findViewById(R.id.Artist_View);
		album_View = (TextView)findViewById(R.id.Album_View);
		year_View = (TextView)findViewById(R.id.Year_View);
		filesize_View = (TextView)findViewById(R.id.Filesize_View);
		position_View = (TextView)findViewById(R.id.Position_View);
		image_View = (ImageView)findViewById(R.id.Image_View);

		service_start.setOnClickListener(this);
		service_end.setOnClickListener(this);
		service_ff.setOnClickListener(this);
		

		list_Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// list_Button.setSelected(true);

				// list_Button.setPressed(true);

				init_phone_audio_grid();

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
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		} else if (v.getId() == R.id.rw) {
			mService.setMusicRewind();
			stopService(new Intent(this, MyService.class));
		} else if (v.getId() == R.id.ff){
	        if (mBound) {
	            // Call a method from the LocalService.
	            // However, if this call were something that might hang, then this request should
	            // occur in a separate thread to avoid slowing down the activity performance.
	            mService.setMusicForward();	        	
	        }
		}

	}

	static class ViewHolder {
		TextView txtTitle;
		TextView txtSize;
		ImageView thumbImage;
		TextView runningTime;
	}

	@SuppressWarnings("deprecation")
	private void init_phone_audio_grid() {
		LinearLayout.setVisibility(View.INVISIBLE);
		List.setVisibility(View.VISIBLE);

		System.gc();
		String[] proj = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.SIZE, 
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.YEAR,
				MediaStore.Audio.Media.SIZE				
				};
		audiocursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
		count = audiocursor.getCount();
		PhoneAideoList = (ListView) findViewById(R.id.PhoneAideoList);
		PhoneAideoList.setAdapter(new audioAdapter(getApplicationContext()));
		PhoneAideoList.setOnItemClickListener(audiogridlistener);
	}

	private OnItemClickListener audiogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {

			System.gc();
			audio_column_index = audiocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			position_View.setText(audiocursor.getString(audio_column_index));
			audiocursor.moveToPosition(position);
			

			filename = audiocursor.getString(audio_column_index);
			// audioview.setaudioPath(filename);
			audiocursor.moveToPosition(position);

			audio_column_index = audiocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
			// movie_Name.setText(audiocursor.getString(audio_column_index));
			audiocursor.moveToPosition(position);

			audio_column_index = audiocursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
			String iduration = audiocursor.getString(audio_column_index);
			//audiocursor.moveToPosition(position);
			
			audio_column_index = audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
			//audiocursor.moveToPosition(position);
			artist_View.setText(audiocursor.getString(audio_column_index));
			
			audio_column_index = audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
			//audiocursor.moveToPosition(position);
			album_View.setText(audiocursor.getString(audio_column_index));
			
			audio_column_index = audiocursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR);
			//audiocursor.moveToPosition(position);
			year_View.setText(audiocursor.getString(audio_column_index));			
			

			long timeInmillisec = Long.parseLong(iduration);
			long duration = timeInmillisec / 1000;
			int hours = (int) (duration / 3600);
			int minutes = (int) ((duration - hours * 3600) / 60);
			int seconds = (int) (duration - (hours * 3600 + minutes * 60));

			String time = String.format("%02d", hours) + ":"
					+ String.format("%02d", minutes) + ":"
					+ String.format("%02d", seconds);
			// timeText2.setText(time);

			// audioview.requestFocus();

			List.setVisibility(View.INVISIBLE);
			LinearLayout.setVisibility(View.VISIBLE);
		}
	};
	
	
	
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
		
	}


	public class audioAdapter extends BaseAdapter {
		private Context vContext;

		public audioAdapter(Context c) {
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

				// ListView
				audio_column_index = audiocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
				audiocursor.moveToPosition(position);
				id = audiocursor.getString(audio_column_index);
				holder.txtTitle.setText(id);

				// ListView
				audio_column_index = audiocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
				audiocursor.moveToPosition(position);
				String size = audiocursor.getString(audio_column_index);

				long lsize = Long.parseLong(size);
				double dsize = lsize / 1000000;
				String ssize = String.format("%4.1f", dsize);
				holder.txtSize.setText(ssize + "Mb" + " | ");
				
				filesize_View.setText(ssize + "Mb");
				
				

				// ListView
				audio_column_index = audiocursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
				audiocursor.moveToPosition(position);
				String iduration = audiocursor.getString(audio_column_index);

				String time;

				long timeInmillisec = Long.parseLong(iduration);
				long duration = timeInmillisec / 1000;
				int hours = (int) (duration / 3600);
				int minutes = (int) ((duration - hours * 3600) / 60);
				int seconds = (int) (duration - (hours * 3600 + minutes * 60));

				time = String.format("%02d", hours) + ":"
						+ String.format("%02d", minutes) + ":"
						+ String.format("%02d", seconds);

				holder.runningTime.setText(time);
				audiocursor.moveToPosition(position);

				String[] proj = { MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.YEAR,
						MediaStore.Audio.Media.SIZE
						};
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(
						MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj,
						MediaStore.Audio.Media.DISPLAY_NAME + "=?",
						new String[] { id }, null);
				cursor.moveToFirst();
				long ids = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID));
				
				
				//
				long albumId = ids;
				Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
				Uri sAlbumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
				image_View.setImageURI(sAlbumArtUri);
				//

				ContentResolver crThumb = getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				// MediaStore.Audio.
				// Bitmap curThumb = MediaStore.Images.Thumbnails(
				// crThumb, ids,Images.Thumbnails.MICRO_KIND.null);
				// Bitmap sizingBmp = Bitmap.createScaledBitmap(curThumb, 140,
				// 90, true);
				// holder.thumbImage.setImageBitmap(curThumb);
				holder.thumbImage.setImageBitmap(null);
				// curThumb = null;
			}
			return convertView;
		}
	}
	

}