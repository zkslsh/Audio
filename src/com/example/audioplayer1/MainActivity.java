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

	private Button back_Button; // BACK ��ư
	private Button home_Button; // HOME ��ư
	private Button menu_Button; // MENU ��ư
	private Button function_Button; // FUNCTION ��ư
	private Button setting_Button; // SETTING ��ư
	private Button play_Button; // PLAY ��ư
	private Button rw_Button; // RW ��ư ( �����ΰ��� )
	private Button ff_Button; // FF ��ư ( �ڷΰ��� )
	private Button list_Button; // LIST ��ư

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