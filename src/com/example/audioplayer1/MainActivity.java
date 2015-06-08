package com.example.audioplayer1;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
	private Button rw_Button; // RW 버튼 ( 앞으로가기 )
	private Button ff_Button; // FF 버튼 ( 뒤로가기 )
	private Button list_Button; // LIST 버튼

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button service_start = (Button) findViewById(R.id.play);
		Button service_end = (Button) findViewById(R.id.rw);

		service_start.setOnClickListener(this);
		service_end.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.play) {
			startService(new Intent(this, MyService.class));
		} else if (v.getId() == R.id.rw) {
			stopService(new Intent(this, MyService.class));
		} else if (v.getId() == R.id.list){
		}
		
	}
}