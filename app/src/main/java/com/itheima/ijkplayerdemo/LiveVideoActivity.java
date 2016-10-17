package com.itheima.ijkplayerdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;

import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.opendanmaku.DanmakuItem;
import com.opendanmaku.DanmakuView;
import com.opendanmaku.IDanmakuItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ccy on 2016/9/7.
 */
public class LiveVideoActivity extends Activity {
	private PlayerView player;
	private PowerManager.WakeLock wakeLock;
	View rootView;
	private DanmakuView mDanmakuView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_video);

		/**常亮*/
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
		wakeLock.acquire();
		player = new PlayerView(this, rootView) {
			@Override
			public PlayerView toggleProcessDurationOrientation() {
				hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
			}
		}
				.setTitle("什么")
				.setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
				.setScaleType(PlayStateParams.fillparent)
				.forbidTouch(false)
				.hideSteam(false)
				.hideCenterPlayer(true)
				.setPlaySource("rtmp://live.hkstv.hk.lxdns.com/live/hks")
				.startPlay();

		mDanmakuView = (DanmakuView) findViewById(R.id.danmaku_view);
//		mDanmakuView.setMaxRow(10);
		mDanmakuView.setMaxRunningPerRow(100);
		List<IDanmakuItem> items = new ArrayList<IDanmakuItem>();
		for (int i = 0; i <10 ; i++) {
			Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
			drawable.setBounds(0,0,100,100);
			ImageSpan is=new ImageSpan(drawable);
			SpannableString ss=new SpannableString("我是图文消息");
			ss.setSpan(is,0,2,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
			IDanmakuItem item = new DanmakuItem(LiveVideoActivity.this, ss, mDanmakuView.getWidth(), 0, android.R.color.holo_red_dark, 18, 1);
			items.add(item);
		}

		mDanmakuView.addItem(items,true);
	}

	Handler handler=new Handler();

	@Override
	protected void onPause() {
		super.onPause();
		if (player != null) {
			player.onPause();
		}
//		/**demo的内容，恢复系统其它媒体的状态*/
//		MediaUtils.muteAudioFocus(mContext, true);
		mDanmakuView.hide();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (player != null) {
			player.onResume();
		}
//		/**demo的内容，暂停系统其它媒体的状态*/
//		MediaUtils.muteAudioFocus(mContext, false);
//		/**demo的内容，激活设备常亮状态*/
//		if (wakeLock != null) {
//			wakeLock.acquire();
//		}

		mDanmakuView.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.onDestroy();
		}

		mDanmakuView.clear();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (player != null) {
			player.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public void onBackPressed() {
		if (player != null && player.onBackPressed()) {
			return;
		}
		super.onBackPressed();
		/**demo的内容，恢复设备亮度状态*/
		if (wakeLock != null) {
			wakeLock.release();
		}
	}

}