package com.example.hufz.myapplication;

import java.io.File;

import android.app.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.ActionBar;

public  class MainActivity extends Activity implements  android.view.GestureDetector.OnGestureListener
{
    //定义手势检测器实例
    GestureDetector detector;

    private TempControlView tempControl;
    private String[] areas = new String[]{"1H","2H", "3H", "4H", "5H", "6H", "7H","8H", "9H", "10H", "11H", "12H", "13H","14H", "15H", "16H", "17H", "18H", "19H","20H", "21H", "22H", "23H" , "24H"};
    private boolean[] areaState=new boolean[]{false, false, false, false, false, true,true, true, false, false, false, true,true, false, false, false, true, true,true, true, true, false, false, false };
    private ListView areaCheckListView;
    private Button Button;
    private View mContentView;
    private Button button;
    private Button Button_video;
    private boolean bcf=true;
    //ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      /*  //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);*/
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
        Button=(Button)findViewById(R.id.Button);
        Button_video=(Button)findViewById(R.id.button_video) ;
       // actionBar=getActionBar();
        //actionBar.hide();
        Button_video.setOnClickListener(new but_videoplay());
        Button.setOnClickListener(new CheckBoxClickListener());
    }
   //----------------------------------------------------------------------------
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
            Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
        }else if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
        }else if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
        }else if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(this,"press",Toast.LENGTH_SHORT).show();
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

    class but_videoplay implements OnClickListener{
        public void onClick (View v){
            Intent intent = null;
            intent=new Intent(MainActivity.this, VideoViewActivity.class);
            startActivity(intent);
        }
    }
    //@Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_video:
                intent=new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent);
                break;
		/*case R.id.btn_controller:
			intent=new Intent(MainActivity.this, ControllerActivity.class);
			startActivity(intent);
			break;*/
            default:
                break;
        }

    }
    public void SetBc (View vt){
       if(bcf) {mContentView.setBackgroundColor(0xff000000);
           bcf=!bcf;
       }
       else {mContentView.setBackgroundColor(0xECF4F9);
           bcf=!bcf;
       }
    }


    class CheckBoxClickListener implements OnClickListener{
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

