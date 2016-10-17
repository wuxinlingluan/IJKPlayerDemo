package com.itheima.ijkplayerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void playDiffCalrityVideos(View view){
		startActivity(new Intent(this,CalrityActivity.class));
	}

	public void playLiveVideo(View view){
		startActivity(new Intent(this,LiveVideoActivity.class));
	}
}
