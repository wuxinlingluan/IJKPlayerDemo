package com.itheima.ijkplayerdemo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ccy on 2016/9/6.
 */
public class CalrityActivity extends AppCompatActivity {
	private PlayerView player;
	private Context mContext;
	private List<VideoijkBean> list;
	private PowerManager.WakeLock wakeLock;
	View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		rootView = getLayoutInflater().from(this).inflate(R.layout.activity_calrity, null);
		setContentView(rootView);
		/**虚拟按键的隐藏方法*/
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				//比较Activity根布局与当前布局的大小
				int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
				if (heightDiff > 100) {
					//大小超过100时，一般为显示虚拟键盘事件
					rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
				} else {
					//大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
					rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

				}
			}
		});

		/**常亮*/
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
		wakeLock.acquire();

		list = new ArrayList<VideoijkBean>();
		String url1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
		String url2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
		VideoijkBean m1 = new VideoijkBean();
		m1.setStream("标清");
		m1.setUrl(url1);
		VideoijkBean m2 = new VideoijkBean();
		m2.setStream("高清");
		m2.setUrl(url2);
		list.add(m1);
		list.add(m2);

		player = new PlayerView(this, rootView) {
			@Override
			public PlayerView toggleProcessDurationOrientation() {
				hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
			}

			@Override
			public PlayerView setPlaySource(List<VideoijkBean> list) {
				return super.setPlaySource(list);
			}
		}
				.setTitle("什么")
				.setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
				.setScaleType(PlayStateParams.fillparent)
				.forbidTouch(false)
				.hideSteam(true)
				.hideCenterPlayer(true)
//				.showThumbnail(new OnShowThumbnailListener() {
//					@Override
//					public void onShowThumbnail(ImageView ivThumbnail) {
//						Glide.with(mContext)
//								.load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
//								.placeholder(R.color.cl_default)
//								.error(R.color.cl_error)
//								.into(ivThumbnail);
//					}
//				})
				.setPlaySource(list)
				.startPlay();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (player != null) {
			player.onPause();
		}
		/**demo的内容，恢复系统其它媒体的状态*/
//		MediaUtils.muteAudioFocus(mContext, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (player != null) {
			player.onResume();
		}
		/**demo的内容，暂停系统其它媒体的状态*/
//		MediaUtils.muteAudioFocus(mContext, false);
		/**demo的内容，激活设备常亮状态*/
		if (wakeLock != null) {
			wakeLock.acquire();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.onDestroy();
		}
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
