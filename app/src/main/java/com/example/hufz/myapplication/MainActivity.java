package com.example.hufz.myapplication;

import java.io.File;

import android.app.Activity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.content.Intent;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.VideoView;

public  class MainActivity extends Activity implements  android.view.GestureDetector.OnGestureListener
{
    //定义手势检测器实例
    private GestureDetector detector;
    private final String TAG = "main";
    private TempControlView tempControl;
    final private String[] areas = new String[]{"1H","2H", "3H", "4H", "5H", "6H", "7H","8H", "9H", "10H", "11H", "12H", "13H","14H", "15H", "16H", "17H", "18H", "19H","20H", "21H", "22H", "23H" , "24H"};
    final private boolean[] areaState=new boolean[]{false, false, false, false, false, true,true, true, false, false, false, true,true, false, false, false, true, true,true, true, true, false, false, false };
    private ListView areaCheckListView;
    private Button Button;
    private View mContentView;
    private Button button;
    private Button Button_video;
    private boolean bcf=true;
    private VideoView videoView;
    //ActionBar actionBar;
    private boolean isPlaying=false;
    private boolean playState =false;
    private boolean BCChanged=false;
    private ScreenObserver mScreenObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //创建手势检测器
        detector = new GestureDetector(this,this);

        button=(Button)findViewById(R.id.button);
        mContentView = findViewById(R.id.fullscreen_content_controls);
        tempControl = (TempControlView) findViewById(R.id.temp_control);
        tempControl.setTemp(25, 85, 25);
        tempControl.setOnTempChangeListener(new TempControlView.OnTempChangeListener() {
            @Override
            public void change(int temp) {
              //  Toast.makeText(MainActivity.this, temp + "°", Toast.LENGTH_SHORT).show();
            }
        });
        videoView=(VideoView)findViewById(R.id.videoView) ;
        Button=(Button)findViewById(R.id.Button);
        Button_video=(Button)findViewById(R.id.button_video) ;
       // actionBar=getActionBar();
        //actionBar.hide();
        Button_video.setOnClickListener(new but_videoplay());
        Button.setOnClickListener(new CheckBoxClickListener());
        //---屏幕监听，-关闭屏幕后，停止视频播放---------------------------------------------------
        /*通过BroadcastReceiver接收广播Intent.ACTION_SCREEN_ON和Intent.ACTION_SCREEN_OFF可以判断屏
        幕状态是否锁屏，但是只有屏幕状态发生改变时才会发出广播；*/
        mScreenObserver = new ScreenObserver(this);
        mScreenObserver.requestScreenStateUpdate(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                doSomethingOnScreenOn();
            }

            @Override
            public void onScreenOff() {
                doSomethingOnScreenOff();
            }
        });
    }
    private void doSomethingOnScreenOn() {
        Log.i(TAG, "Screen is on");
       if (playState) {
           play(0);
       }
    }

    private void doSomethingOnScreenOff() {
        Log.i(TAG, "Screen is off");
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止监听screen状态
        mScreenObserver.stopScreenStateUpdate();
    }

   //------------------------------------------------------------------------------------------
    //将该activity上的触碰事件交给GestureDetector处理
    public boolean onTouchEvent(MotionEvent me){
        return detector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    /**
     * 滑屏监测
     *
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            tempControl.setVisibility(View.INVISIBLE);
            Button.setVisibility(View.INVISIBLE);
            videoView.setVisibility(View.VISIBLE);
            //Button_video.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            playState=true;
            if(bcf) {mContentView.setBackgroundColor(0xff000000);
                //bcf=!bcf;
                BCChanged=true;
                Log.i(TAG, "--setBackgroundColor(0xff000000)--");
            }
            play(0);
            //Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
           stop();
            tempControl.setVisibility(View.VISIBLE);
            Button.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
           // Button_video.setVisibility(View.VISIBLE);
            //button.setVisibility(View.VISIBLE);
            playState=false;
            Log.i(TAG, "----右滑，Play stoped,playstate=false");
            if(BCChanged) {mContentView.setBackgroundColor(0xECF4F9);
                BCChanged=!BCChanged;
                Log.i(TAG, "BCChanged,---:setBackgroundColor(0xECF4F9)---");
            }
            //Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
           // Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
            SetBc(mContentView);
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
           // Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
            SetBc(mContentView);
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //Toast.makeText(this,"press",Toast.LENGTH_SHORT).show();
        SetBc(mContentView);

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX,
                            float velocityY) {

        return false;
    }
    //----------------------------------------------------------------------
    private void play(int msec) {
       // Log.i(TAG, "------------------play-------------------------------");
        String path = Environment.getExternalStorageDirectory().getPath()+"/"+"1.mp4";
        //  et_path.getText().toString().trim();
        //String path = "/mnt/sdcard/1.mp4";  //  et_path.getText().toString().trim();
        Log.i(TAG, "playPath:"+path);
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "File Not exists", Toast.LENGTH_SHORT).show();
            return;
        }

       // Log.i(TAG, "------play----文件存在");
        videoView.setVideoPath(file.getAbsolutePath());
        //Log.i(TAG, "------play----已经设定文件地址");
        videoView.seekTo(msec);
        videoView.start();
        isPlaying = true;
        playState = true;
        Log.i(TAG, "------------------play-------------------------------");
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
       // btn_play.setEnabled(false);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //监控播放结束，播放结束后自动重播
            @Override
            public void onCompletion(MediaPlayer mp) {
                //
                //btn_play.setEnabled(true);
               // stop();
                //videoView.start();
                play(0);

            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

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

    /*protected void replay() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.seekTo(3000);
            Toast.makeText(this, "循环", Toast.LENGTH_LONG).show();
            //btn_pause.setText("||");
            return;
        }
        isPlaying = false;
        play(0);

    }*/

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
	private  void stop() {
		if (videoView != null && videoView.isPlaying()) {
			videoView.stopPlayback();
			videoView.setEnabled(true);
			isPlaying = false;
           // playState=false;
           // Log.i(TAG, "----------------Stop--play---Playstate=:"+playState);
		}
	}
	//---------------------------------------------------------------------------------------------
    private class but_videoplay implements OnClickListener{
        //通过按钮切换到VideoViewActivity
        public void onClick (View v){
            Intent intent;
            intent=new Intent(MainActivity.this, VideoViewActivity.class);
            startActivity(intent);
        }
    }

    //@Override
   /* public void onClick(View v) {
        //通过按钮切换到VideoViewActivity
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_video:
                intent=new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent);
                break;
		*//*case R.id.btn_controller:
			intent=new Intent(MainActivity.this, ControllerActivity.class);
			startActivity(intent);
			break;*//*
            default:
                break;
        }

    }*/
    //--------------------------------------------------------------------------------

    public void SetBc (View vt){
       //改变背景色
       if(bcf) {mContentView.setBackgroundColor(0xff000000);
           bcf=!bcf;
       }
       else {mContentView.setBackgroundColor(0xECF4F9);
           bcf=!bcf;
       }
    }


    private class CheckBoxClickListener implements OnClickListener{
        @Override
        public void onClick(View v) {
            AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("运行时间")
                    .setMultiChoiceItems(areas,areaState,new DialogInterface.OnMultiChoiceClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked){
                            //点击某个区域
                        }
                    }).setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog,int whichButton){
                            String s = "您选择了:";
                            for (int i = 0; i < areas.length; i++){
                                if (areaCheckListView.getCheckedItemPositions().get(i)){
                                    s += i + ":"+ areaCheckListView.getAdapter().getItem(i)+ "  ";
                                }else{
                                    areaCheckListView.getCheckedItemPositions().get(i,false);
                                }
                            }
                            if (areaCheckListView.getCheckedItemPositions().size() > 0){
                                //Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            }else{
                                //没有选择
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", null).create();
            areaCheckListView = ad.getListView();
            ad.show();
        }
    }



}

