package com.example.factorymode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ztemt.test.basic.R;


@SuppressLint("ParserError")
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //蓝牙按钮
        Button blutooth=(Button)findViewById(R.id.button_blu);
        blutooth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent blu_intent=new Intent(MainActivity.this,Blutooth.class);
				startActivity(blu_intent);
			}
		});
        //耳机测试按钮
        Button headset=(Button)findViewById(R.id.button_head);
        headset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent head_intent=new Intent(MainActivity.this,headset.class);
				startActivity(head_intent);
			}
		});
        //听筒测试按钮
        Button earphone=(Button)findViewById(R.id.button_ear);
        earphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ear_intent=new Intent(MainActivity.this,Earphone.class);
				startActivity(ear_intent);
			}
		});
        Button microphone=(Button)findViewById(R.id.button_microphone);
        microphone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ear_intent=new Intent(MainActivity.this,MicroPhone.class);
				startActivity(ear_intent);
			}
		});
        Button Vibrator=(Button)findViewById(R.id.button_vibrator);
        Vibrator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ear_intent=new Intent(MainActivity.this,Vibra.class);
				startActivity(ear_intent);
			}
		});
    
    }
   

    
   

}
