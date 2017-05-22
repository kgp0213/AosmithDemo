package com.example.hufz.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hufz.myapplication.TempControlView;
import android.app.ActionBar;


public class MainActivity extends AppCompatActivity {

    private TempControlView tempControl;
    private String[] areas = new String[]{"1H","2H", "3H", "4H", "5H", "6H", "7H","8H", "9H", "10H", "11H", "12H", "13H","14H", "15H", "16H", "17H", "18H", "19H","20H", "21H", "22H", "23H" , "24H"};
    private boolean[] areaState=new boolean[]{false, false, false, false, false, true,true, true, false, false, false, true,true, false, false, false, true, true,true, true, true, false, false, false };
    private ListView areaCheckListView;
    private Button Button;
    private View mContentView;
    private Button button;
    private boolean bcf=true;
    ActionBar actionBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
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
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Button=(Button)findViewById(R.id.Button);
        actionBar=getActionBar();
        //actionBar.hide();
        Button.setOnClickListener(new CheckBoxClickListener());
        //
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
                        public void onClick(DialogInterface dialog,int whichButton, boolean isChecked){
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

