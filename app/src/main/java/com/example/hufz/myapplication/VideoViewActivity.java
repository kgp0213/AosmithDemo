package com.example.hufz.myapplication;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;

public class VideoViewActivity extends Activity {
	private final String TAG = "main";
	private EditText et_path;
	private Button btn_play, btn_replay;
	//private SeekBar seekBar;
	private VideoView vv_video;
	private boolean isPlaying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoview);

		//seekBar = (SeekBar) findViewById(R.id.seekBar);
		//et_path = (EditText) findViewById(R.id.et_path);
		vv_video = (VideoView) findViewById(R.id.vv_videoview);

		btn_play = (Button) findViewById(R.id.btn_play);
		//btn_pause = (Button) findViewById(R.id.btn_pause);
		btn_replay = (Button) findViewById(R.id.btn_replay);
		//btn_stop = (Button) findViewById(R.id.btn_stop);

		btn_play.setOnClickListener(click);
		//btn_pause.setOnClickListener(click);
		btn_replay.setOnClickListener(click);
		//btn_stop.setOnClickListener(click);

		//
		//seekBar.setOnSeekBarChangeListener(change);
	}

	/*private OnSeekBarChangeListener change = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

			int progress = seekBar.getProgress();
			if (vv_video != null && vv_video.isPlaying()) {

				vv_video.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};*/
	private View.OnClickListener click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_play:
				play(0);
				break;
			/*case R.id.btn_pause:
				pause();
				break;*/
			case R.id.btn_replay:
				replay();
				break;
			/*case R.id.btn_stop:
				stop();
				break;*/
			default:
				break;
			}
		}
	};

	protected void play(int msec) {
		Log.i(TAG, "Play");
		String path ="sdcard/1.mp4";  //  et_path.getText().toString().trim();
		File file = new File(path);
		if (!file.exists()) {
			Toast.makeText(this, "File Not exists", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Log.i(TAG, "文件存在");
		vv_video.setVideoPath(file.getAbsolutePath());
		Log.i(TAG, "获得文件地址");
		vv_video.seekTo(msec);
		vv_video.start();
		//vv_video.onTouchEvent()
		//

		//
		//seekBar.setMax(vv_video.getDuration());

		//
		/*new Thread() {

			@Override
			public void run() {
				try {
					isPlaying = true;
					while (isPlaying) {

						int current = vv_video.getCurrentPosition();
						//seekBar.setProgress(current);

						sleep(500);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
		//
		btn_play.setEnabled(false);

		vv_video.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				//
				//btn_play.setEnabled(true);
				vv_video.start();

			}
		});

		vv_video.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//
				play(0);
				isPlaying = false;
				return false;
			}
		});
	}
	/*@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(vv_video.isPlaying()){
			vv_video.stopPlayback();
		}
		//vv_video.release();
		//Activity销毁时停止播放，释放资源。不做这个操作，即使退出还是能听到视频播放的声音
	}*/
	/**
	 * //
	 */
	protected void replay() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.seekTo(3000);
			Toast.makeText(this, "循环", Toast.LENGTH_LONG).show();
			//btn_pause.setText("||");
			return;
		}
		isPlaying = false;
		play(0);

	}

	/**
	 * ��ͣ�����
	 */
	/*protected void pause() {
		if (btn_pause.getText().toString().trim().equals("����")) {
			btn_pause.setText("��ͣ");
			vv_video.start();
			Toast.makeText(this, "��������", 0).show();
			return;
		}
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.pause();
			btn_pause.setText("����");
			Toast.makeText(this, "��ͣ����", 0).show();
		}
	}*/

	/*
	 * ֹͣ����
	 */
	/*protected void stop() {
		if (vv_video != null && vv_video.isPlaying()) {
			vv_video.stopPlayback();
			btn_play.setEnabled(true);
			isPlaying = false;
		}
	}*/
}
